package dev.mikhalchenkov.isxcatalogviewer.core.data.repositories

import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.CatalogDataSourceImpl
import dev.mikhalchenkov.isxcatalogviewer.core.data.mappers.CatalogItemDtoMapper.toDomain
import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogItemDto
import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogResponseDto
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CatalogRepositoryImplTest {
    private val dataSource = mockk<CatalogDataSourceImpl>(relaxed = true)
    private lateinit var repository: CatalogRepository

    @Before
    fun setUp() {
        repository = CatalogRepositoryImpl(dataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getAll emits success with mapped list`() = runTest {
        // given
        val dtoItems = listOf(
            CatalogItemDto("bk_001", "The Blue Fox", "Fiction", 12.99, 4.4),
            CatalogItemDto("bk_002", "Data Sketches", "Non-Fiction", 32.00, 4.8),
        )
        coEvery { dataSource.getCatalog() } returns CatalogResponseDto(
            updatedAt = "2025-01-15T10:00:00Z",
            items = dtoItems
        )
        val expected = dtoItems.map { it.toDomain() }

        // when
        val actual = repository.getAll().first()

        // then
        assertEquals(expected, actual)
        coVerify(exactly = 1) { dataSource.getCatalog() }
    }

    @Test
    fun `findById returns mapped item when found`() = runTest {
        // given
        val target = CatalogItemDto("bk_004", "Kotlin by Example", "Tech", 21.0, 4.3)
        val dtoItems = listOf(
            CatalogItemDto("bk_003", "Swift Patterns", "Tech", 24.5, 4.1),
            target,
            CatalogItemDto("bk_005", "Windswept", "Fiction", 14.25, 3.9),
        )
        coEvery { dataSource.getCatalog() } returns CatalogResponseDto(
            updatedAt = "2025-01-15T10:00:00Z",
            items = dtoItems
        )

        // when
        val result = repository.findById("bk_004")

        // then
        assertEquals(target.toDomain(), result)
        coVerify(exactly = 1) { dataSource.getCatalog() }
    }

    @Test
    fun `findById returns success with null when not found`() = runTest {
        // given
        val dtoItems = listOf(
            CatalogItemDto("bk_001", "The Blue Fox", "Fiction", 12.99, 4.4),
            CatalogItemDto("bk_002", "Data Sketches", "Non-Fiction", 32.00, 4.8),
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
    fun `getAll throws when dataSource throws`() = runTest {
        // given
        coEvery { dataSource.getCatalog() } throws IOException("boom")

        // then
        try {
            repository.getAll().first()   // suspend — можно вызывать в runTest
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

        // then
        try {
            repository.findById("any")    // suspend
            fail("Expected IllegalStateException to be thrown")
        } catch (e: IllegalStateException) {
            // ok
        }

        coVerify(exactly = 1) { dataSource.getCatalog() }
    }
}