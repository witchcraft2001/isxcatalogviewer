package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl

import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.usecases.ToggleFavoriteUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.domain.GetCatalogItemByIdUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogDetailsState
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogDetailsViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CatalogDetailsViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var getCatalogItemByIdUseCase: GetCatalogItemByIdUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    private lateinit var viewModel: CatalogDetailsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getCatalogItemByIdUseCase = mockk()
        toggleFavoriteUseCase = mockk(relaxed = true)

        viewModel = CatalogDetailsViewModel(
            getCatalogItemByIdUseCase,
            toggleFavoriteUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state should be Loading`() {
        // Then
        assertEquals(CatalogDetailsState.Loading, viewModel.state.value)
    }

    @Test
    fun `loadItem should emit Show state when item is found successfully`() = runTest {
        // Given
        val itemId = "123"
        val catalogItem = CatalogItemFavorite(
            id = itemId,
            title = "Test Item",
            category = "Test Category",
            price = 99.99,
            rating = 4.5,
            isFavorite = false
        )

        every { getCatalogItemByIdUseCase(itemId) } returns flowOf(AsyncResult.Success(catalogItem))

        // When
        viewModel.loadItem(itemId)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is CatalogDetailsState.Show)

        val showState = state as CatalogDetailsState.Show
        assertEquals(itemId, showState.item.id)
        assertEquals("Test Item", showState.item.title)
        assertEquals("Test Category", showState.item.category)
        assertEquals("$99.99", showState.item.price)
        assertEquals("4.5", showState.item.rating)
        assertEquals(false, showState.item.isFavorite)

        verify(exactly = 1) { getCatalogItemByIdUseCase(itemId) }
    }

    @Test
    fun `loadItem should handle multiple emissions from use case`() = runTest {
        // Given
        val itemId = "123"
        val firstItem = CatalogItemFavorite(
            id = itemId,
            title = "Item 1",
            category = "Category 1",
            price = 50.0,
            rating = 4.0,
            isFavorite = false
        )
        val secondItem = CatalogItemFavorite(
            id = itemId,
            title = "Item 2",
            category = "Category 2",
            price = 75.0,
            rating = 4.5,
            isFavorite = true
        )

        every { getCatalogItemByIdUseCase(itemId) } returns flow {
            emit(AsyncResult.Success(firstItem))
            emit(AsyncResult.Success(secondItem))
        }

        // When
        viewModel.loadItem(itemId)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is CatalogDetailsState.Show)

        val showState = state as CatalogDetailsState.Show
        assertEquals("Item 2", showState.item.title)
        assertEquals("Category 2", showState.item.category)
        assertEquals("$75.00", showState.item.price)
        assertEquals("4.5", showState.item.rating)
        assertEquals(true, showState.item.isFavorite)
    }

    @Test
    fun `loadItem should emit NotFound state when item is null`() = runTest {
        // Given
        val itemId = "123"
        every { getCatalogItemByIdUseCase(itemId) } returns flowOf(AsyncResult.Success(null))

        // When
        viewModel.loadItem(itemId)
        advanceUntilIdle()

        // Then
        assertEquals(CatalogDetailsState.NotFound, viewModel.state.value)
        verify(exactly = 1) { getCatalogItemByIdUseCase(itemId) }
    }

    @Test
    fun `loadItem should emit Error state when use case fails`() = runTest {
        // Given
        val itemId = "123"
        val exception = Exception("Failed to load item")
        every { getCatalogItemByIdUseCase(itemId) } returns flowOf(AsyncResult.Error(exception))

        // When
        viewModel.loadItem(itemId)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is CatalogDetailsState.Error)
        verify(exactly = 1) { getCatalogItemByIdUseCase(itemId) }
    }

    @Test
    fun `onToggleFavorite should call toggleFavoriteUseCase successfully`() = runTest {
        // Given
        val itemId = "123"
        coEvery { toggleFavoriteUseCase(itemId) } just Runs

        // When
        viewModel.onToggleFavorite(itemId)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { toggleFavoriteUseCase(itemId) }
        assertFalse(viewModel.state.value is CatalogDetailsState.Error)
    }

    @Test
    fun `onToggleFavorite should emit Error state when use case throws exception`() = runTest {
        // Given
        val itemId = "123"
        val exception = Exception("Failed to toggle favorite")
        coEvery { toggleFavoriteUseCase(itemId) } throws exception

        // When
        viewModel.onToggleFavorite(itemId)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is CatalogDetailsState.Error)
        coVerify(exactly = 1) { toggleFavoriteUseCase(itemId) }
    }

    @Test
    fun `loadItem with error then success should update state correctly`() = runTest {
        // Given
        val itemId = "123"
        val catalogItem = CatalogItemFavorite(
            id = itemId,
            title = "Test Item",
            category = "Test Category",
            price = 99.99,
            rating = 4.5,
            isFavorite = false
        )

        every { getCatalogItemByIdUseCase(itemId) } returnsMany listOf(
            flowOf(AsyncResult.Error(Exception("Network error"))),
            flowOf(AsyncResult.Success(catalogItem))
        )

        // When
        viewModel.loadItem(itemId)
        advanceUntilIdle()
        assertTrue(viewModel.state.value is CatalogDetailsState.Error)

        viewModel.loadItem(itemId)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is CatalogDetailsState.Show)
        assertEquals("Test Item", (state as CatalogDetailsState.Show).item.title)
        verify(exactly = 2) { getCatalogItemByIdUseCase(itemId) }
    }
}