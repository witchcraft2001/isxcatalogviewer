package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.usecases.ToggleFavoriteUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases.ObserveCatalogItemsUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.mappers.toUi
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getCatalogItemsUseCase: ObserveCatalogItemsUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var itemsFlow: MutableStateFlow<AsyncResult<List<CatalogItemFavorite>>>

    private lateinit var viewModel: CatalogViewModel
    private val testItem1 = CatalogItemFavorite(
        id = "id1",
        title = "Title 1",
        category = "Cat A",
        price = 10.0,
        rating = 4.5,
        isFavorite = false
    )
    private val testItem2 = CatalogItemFavorite(
        id = "id2",
        title = "Title 2",
        category = "Cat B",
        price = 20.0,
        rating = 3.5,
        isFavorite = true
    )
    private val testList = listOf(testItem1, testItem2)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getCatalogItemsUseCase = mockk()
        toggleFavoriteUseCase = mockk(relaxed = true)

        itemsFlow = MutableStateFlow(AsyncResult.Loading)
        every { getCatalogItemsUseCase(any(), any()) } returns itemsFlow

        viewModel = CatalogViewModel(
            getCatalogItemsUseCase,
            toggleFavoriteUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest(testDispatcher) {
        // given
        // when
        // then
        assertEquals(CatalogViewState.Loading, viewModel.state.value)
    }

    @Test
    fun `success maps to Show with UI items`() = runTest(testDispatcher) {
        // given
        val collectedStates = mutableListOf<CatalogViewState>()
        val job = launch {
            viewModel.state.collect { collectedStates.add(it) }
        }
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(CatalogViewState.Loading, collectedStates.last())

        // when
        itemsFlow.value = AsyncResult.Success(testList)
        testDispatcher.scheduler.advanceUntilIdle()

        // then
        val expectedUiList = testList.map { it.toUi() }
        val expectedState = CatalogViewState.Show(expectedUiList, "")
        assertEquals(expectedState, collectedStates.last())
        job.cancel()
    }

    @Test
    fun `onQueryChanged should update state with new query`() = runTest(testDispatcher) {
        // given
        val collectedStates = mutableListOf<CatalogViewState>()
        val job = launch {
            viewModel.state.collect { collectedStates.add(it) }
        }

        itemsFlow.value = AsyncResult.Success(testList)
        testDispatcher.scheduler.advanceUntilIdle()

        val expectedUiList = testList.map { it.toUi() }
        val initialState = CatalogViewState.Show(expectedUiList, "")
        assertEquals(initialState, collectedStates.last())

        // when
        val newQuery = "Title 1"
        viewModel.onQueryChanged(newQuery)
        testDispatcher.scheduler.advanceUntilIdle()

        // then
        val newState = collectedStates.last()
        assertTrue(newState is CatalogViewState.Show)
        assertEquals(newQuery, (newState as CatalogViewState.Show).query)
        assertEquals(expectedUiList, newState.items)
        job.cancel()
    }

    @Test
    fun `error maps to Error with message`() = runTest(testDispatcher) {
        // given
        val collectedStates = mutableListOf<CatalogViewState>()
        val job = launch {
            viewModel.state.collect { collectedStates.add(it) }
        }
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(CatalogViewState.Loading, collectedStates.last())

        // when
        val exception = RuntimeException("Network Error")
        itemsFlow.value = AsyncResult.Error(exception)
        testDispatcher.scheduler.advanceUntilIdle()

        // then
        val expectedState = CatalogViewState.Error("Network Error")
        assertEquals(expectedState, collectedStates.last())
        job.cancel()
    }

    @Test
    fun `onToggleFavorite should call toggleFavoriteUseCase and emit success message`() = runTest {
        // given
        val itemId = "123"
        coEvery { toggleFavoriteUseCase(itemId) } just Runs

        // when
        val eventDeferred = async { viewModel.events.first() }
        viewModel.onToggleFavorite(itemId)
        val event = eventDeferred.await()

        // then
        coVerify(exactly = 1) { toggleFavoriteUseCase(itemId) }
        TestCase.assertFalse(viewModel.state.value is CatalogViewState.Error)
        TestCase.assertTrue(event is CatalogEvent.ShowMessage)
        TestCase.assertTrue((event as CatalogEvent.ShowMessage).text.contains("Favorite"))
    }

    @Test
    fun `onToggleFavorite should emit Error state when use case throws exception`() = runTest {
        // given
        val itemId = "123"
        val exception = Exception("Failed to toggle favorite")
        coEvery { toggleFavoriteUseCase(itemId) } throws exception

        // when
        val eventDeferred = async { viewModel.events.first() }
        viewModel.onToggleFavorite(itemId)
        val event = eventDeferred.await()
        advanceUntilIdle()

        // then
        TestCase.assertFalse(viewModel.state.value is CatalogViewState.Error)
        TestCase.assertTrue(event is CatalogEvent.ShowMessage)
        TestCase.assertTrue((event as CatalogEvent.ShowMessage).text.contains("Failed"))
        coVerify(exactly = 1) { toggleFavoriteUseCase(itemId) }
    }
}
