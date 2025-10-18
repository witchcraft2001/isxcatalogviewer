package dev.mikhalchenkov.isxcatalogviewer.domain.repositories

import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    suspend fun getAll(): Flow<Result<List<CatalogItem>>>
    suspend fun findById(id: String): Result<CatalogItem?>
}