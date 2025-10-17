package dev.mikhalchenkov.isxcatalogviewer.core.data.repositories

import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.CatalogDataSource
import dev.mikhalchenkov.isxcatalogviewer.core.data.mappers.CatalogItemDtoMapper.toDomain
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CatalogRepositoryImpl constructor(
    private val catalogDataSource: CatalogDataSource
) : CatalogRepository {
    override suspend fun getAll(): Flow<Result<List<CatalogItem>>> = flow {
        try {
            emit(Result.success(catalogDataSource.getCatalog().items.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun findById(id: String): Result<CatalogItem?> {
        return try {
            val result =
                catalogDataSource.getCatalog().items.firstOrNull { it.id == id }?.toDomain()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}