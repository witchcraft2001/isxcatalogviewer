package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases

import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveCatalogItemsUseCaseTest {
    private val catalogRepository = mockk<CatalogRepository>(relaxed = true)
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private lateinit var useCase: ObserveCatalogItemsUseCase

    @Before
    fun setUp() {
        useCase = ObserveCatalogItemsUseCase(catalogRepository, favoriteRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `emits success with correct favorite flags`() = runTest {
        // given
        val items = listOf(
            CatalogItem(
                id = "bk_001",
                title = "The Blue Fox",
                category = "Fiction",
                price = 12.99,
                rating = 4.4,
            ),
            CatalogItem(
                id = "bk_002",
                title = "Data Sketches",
                category = "Non-Fiction",
                price = 32.00,
                rating = 4.8,
            ),
        )
        val catalogFlow = MutableStateFlow(items)
        val favoritesFlow = MutableStateFlow(setOf("bk_002"))
        val refresh = MutableSharedFlow<Unit>()

        every { catalogRepository.getAll() } returns catalogFlow
        every { favoriteRepository.favoriteIds } returns favoritesFlow

        // when
        val emissions = mutableListOf<AsyncResult<List<CatalogItemFavorite>>>()
        val job = launch {
            useCase(refresh).take(2).toList(emissions) // Loading + Success
        }
        runCurrent()
        job.join()

        // then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is AsyncResult.Loading)

        val success = emissions[1] as AsyncResult.Success<List<CatalogItemFavorite>>
        val pairs = success.value.map { it.id to it.isFavorite }
        assertEquals(listOf("bk_001" to false, "bk_002" to true), pairs)
    }

    @Test
    fun `updates favorites produce new emission with updated flags`() = runTest {
        // given
        val items = listOf(
            CatalogItem(
                id = "bk_001",
                title = "The Blue Fox",
                category = "Fiction",
                price = 12.99,
                rating = 4.4,
            ),
            CatalogItem(
                id = "bk_002",
                title = "Data Sketches",
                category = "Non-Fiction",
                price = 32.00,
                rating = 4.8,
            ),
        )
        val catalogFlow = MutableStateFlow(items)
        val favoritesFlow = MutableStateFlow(setOf("bk_001"))
        val refresh = MutableSharedFlow<Unit>()

        every { catalogRepository.getAll() } returns catalogFlow
        every { favoriteRepository.favoriteIds } returns favoritesFlow

        // when
        val successes = mutableListOf<AsyncResult.Success<List<CatalogItemFavorite>>>()
        val job = launch {
            useCase(refresh)
                .filterIsInstance<AsyncResult.Success<List<CatalogItemFavorite>>>()
                .take(2)
                .toList(successes)
        }
        runCurrent()

        favoritesFlow.value = setOf("bk_002")
        runCurrent()
        job.join()

        // then
        assertEquals(2, successes.size)

        val firstPairs = successes[0].value.map { it.id to it.isFavorite }
        assertEquals(listOf("bk_001" to true, "bk_002" to false), firstPairs)

        val secondPairs = successes[1].value.map { it.id to it.isFavorite }
        assertEquals(listOf("bk_001" to false, "bk_002" to true), secondPairs)
    }

    @Test
    fun `propagates failure from repository`() = runTest {
        // given
        val refresh = MutableSharedFlow<Unit>()
        val failure = IllegalStateException("boom")
        every { catalogRepository.getAll() } returns flow { throw failure }
        every { favoriteRepository.favoriteIds } returns MutableStateFlow(emptySet())

        // when
        val emissions = mutableListOf<AsyncResult<List<CatalogItemFavorite>>>()
        val job = launch {
            useCase(refresh).take(2).toList(emissions)
        }
        runCurrent()
        job.join()

        // then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is AsyncResult.Loading)

        val error = emissions[1] as AsyncResult.Error
        assertTrue(error.throwable is IllegalStateException)
        assertEquals("boom", error.throwable.message)
    }
}