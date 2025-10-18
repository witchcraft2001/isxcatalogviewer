package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases

import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetCatalogItemsUseCaseTest {
    private val catalogRepository = mockk<CatalogRepository>(relaxed = true)
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private lateinit var useCase: GetCatalogItemsUseCase

    @Before
    fun setUp() {
        useCase = GetCatalogItemsUseCase(catalogRepository, favoriteRepository)
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
                rating = 4.4
            ),
            CatalogItem(
                id = "bk_002",
                title = "Data Sketches",
                category = "Non-Fiction",
                price = 32.00,
                rating = 4.8
            ),
        )
        val catalogFlow = MutableStateFlow(Result.success(items))
        val favoritesFlow = MutableStateFlow(setOf("bk_002"))

        every { catalogRepository.getAll() } returns catalogFlow
        every { favoriteRepository.favoriteIds } returns favoritesFlow

        // when
        val result = useCase().first()

        // then
        assertTrue(result.isSuccess)
        val mapped = result.getOrNull()!!
        val pairs = mapped.map { it.id to it.isFavorite }
        assertEquals(listOf("bk_001" to false, "bk_002" to true), pairs)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updates favorites produce new emission with updated flags`() = runTest {
        // given
        val items = listOf(
            CatalogItem(
                id = "bk_001",
                title = "The Blue Fox",
                category = "Fiction",
                price = 12.99,
                rating = 4.4
            ),
            CatalogItem(
                id = "bk_002",
                title = "Data Sketches",
                category = "Non-Fiction",
                price = 32.00,
                rating = 4.8
            ),
        )
        val catalogFlow = MutableStateFlow(Result.success(items))
        val favoritesFlow = MutableStateFlow(setOf("bk_001"))

        every { catalogRepository.getAll() } returns catalogFlow
        every { favoriteRepository.favoriteIds } returns favoritesFlow

        // when
        val emissions = mutableListOf<Result<List<CatalogItemFavorite>>>()
        val job = backgroundScope.launch {
            useCase().take(2).toList(emissions)
        }

        runCurrent()

        favoritesFlow.value = setOf("bk_002")
        job.join()

        // then
        assertEquals(2, emissions.size)

        val firstPairs = emissions[0].getOrNull()!!.map { it.id to it.isFavorite }
        assertEquals(listOf("bk_001" to true, "bk_002" to false), firstPairs)

        val secondPairs = emissions[1].getOrNull()!!.map { it.id to it.isFavorite }
        assertEquals(listOf("bk_001" to false, "bk_002" to true), secondPairs)
    }

    @Test
    fun `propagates failure from repository`() = runTest {
        // given
        val failure = IllegalStateException("boom")
        val catalogFlow = MutableStateFlow<Result<List<CatalogItem>>>(Result.failure(failure))
        val favoritesFlow = MutableStateFlow(emptySet<String>())

        every { catalogRepository.getAll() } returns catalogFlow
        every { favoriteRepository.favoriteIds } returns favoritesFlow

        // when
        val result = useCase().first()

        // then
        assertTrue(result.isFailure)
        assertSame(failure, result.exceptionOrNull())
    }
}