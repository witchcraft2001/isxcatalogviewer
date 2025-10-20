package dev.mikhalchenkov.isxcatalogviewer.core.data.repositories

import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.CatalogDataSource
import dev.mikhalchenkov.isxcatalogviewer.core.data.mappers.CatalogItemDtoMapper.toDomain
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val catalogDataSource: CatalogDataSource

) : CatalogRepository {
    override fun getAll(): Flow<List<CatalogItem>> = flow {
        emit(catalogDataSource.getCatalog().items.map { it.toDomain() })
    }

    override suspend fun findById(id: String): CatalogItem? =
        catalogDataSource.getCatalog().items.firstOrNull { it.id == id }?.toDomain()
}
