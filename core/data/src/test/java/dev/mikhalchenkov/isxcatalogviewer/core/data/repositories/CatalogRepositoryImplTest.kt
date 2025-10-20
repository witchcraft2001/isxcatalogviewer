package dev.mikhalchenkov.isxcatalogviewer.core.data.repositories

import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.CatalogDataSourceImpl
import dev.mikhalchenkov.isxcatalogviewer.core.data.mappers.CatalogItemDtoMapper.toDomain
import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogItemDto
import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogResponseDto
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogRepositoryImplTest {
    private val item1 = CatalogItemDto(
        id = "bk_001",
        title = "The Blue Fox",
        category = "Fiction",
        price = 12.99,
        rating = 4.4,
    )
    private val item2 = CatalogItemDto(
        id = "bk_002",
        title = "Data Sketches",
        category = "Non-Fiction",
        price = 32.00,
        rating = 4.8,
    )
    private val item3 = CatalogItemDto(
        id = "bk_003",
        title = "Kotlin by Example",
        category = "Tech",
        price = 21.0,
        rating = 4.3,
    )
    private val item4 = CatalogItemDto(
        id = "bk_004",
        title = "Kotlin by Example",
        category = "Tech",
        price = 21.0,
        rating = 4.3,
    )
    private val item5 = CatalogItemDto(
        id = "bk_005",
        title = "Windswept",
        category = "Fiction",
        price = 14.25,
        rating = 3.9,
    )

    private val dataSource = mockk<CatalogDataSourceImpl>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: CatalogRepository

    @Before
    fun setUp() {
        repository = CatalogRepositoryImpl(dataSource, testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `search with blank query returns all mapped items`() = runTest {
        // given
        val dtoItems = listOf(
            item1,
            item2,
        )
        coEvery { dataSource.getCatalog() } returns CatalogResponseDto(
            updatedAt = "2025-01-15T10:00:00Z",
            items = dtoItems
        )
        val expected: List<CatalogItem> = dtoItems.map { it.toDomain() }

        // when
        val actual = repository.search("").first()

        // then
        assertEquals(expected, actual)
        coVerify(exactly = 1) { dataSource.getCatalog() }
    }

    @Test
    fun `search filters by title case-insensitively`() = runTest {
        // given
        val dtoItems = listOf(
            item1,
            item2,
            item3
        )
        coEvery { dataSource.getCatalog() } returns CatalogResponseDto(
            updatedAt = "2025-01-15T10:00:00Z",
            items = dtoItems
        )

        // when
        val actual = repository.search("kOtLiN").first()

        // then
        assertEquals(listOf(dtoItems[2].toDomain()), actual)
        coVerify(exactly = 1) { dataSource.getCatalog() }
    }

    @Test
    fun `findById returns mapped item when found`() = runTest {
        // given
        val target = item4
        val dtoItems = listOf(
            item3,
            target,
            item5,
        )
        coEvery { dataSource.getCatalog() } returns CatalogResponseDto(
            updatedAt = "2025-01-15T10:00:00Z",
            items = dtoItems
        )

        // then
        val result = repository.findById(target.id)

        assertEquals(target.toDomain(), result)
        coVerify(exactly = 1) { dataSource.getCatalog() }
    }

    @Test
    fun `findById returns null when not found`() = runTest {
        // given
        val dtoItems = listOf(
            item1,
            item2,
        )
        coEvery { dataSource.getCatalog() } returns CatalogResponseDto(
            updatedAt = "2025-01-15T10:00:00Z",
            items = dtoItems
        )

        // when
        val result = repository.findById("missing_id")

        // then
        assertNull(result)
        coVerify(exactly = 1) { dataSource.getCatalog() }
    }

    @Test
    fun `search throws when dataSource throws`() = runTest {
        // given
        coEvery { dataSource.getCatalog() } throws IOException("boom")

        // when
        // then
        try {
            repository.search("").first()
            fail("Expected IOException to be thrown")
        } catch (e: IOException) {
            // ok
        }

        coVerify(exactly = 1) { dataSource.getCatalog() }
    }

    @Test
    fun `findById throws when dataSource throws`() = runTest {
        // given
        coEvery { dataSource.getCatalog() } throws IllegalStateException("bad data")

        // when
        // then
        try {
            repository.findById("any")
            fail("Expected IllegalStateException to be thrown")
        } catch (e: IllegalStateException) {
            // ok
        }

        coVerify(exactly = 1) { dataSource.getCatalog() }
    }
}