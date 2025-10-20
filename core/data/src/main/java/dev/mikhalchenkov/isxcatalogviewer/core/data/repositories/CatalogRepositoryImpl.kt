package dev.mikhalchenkov.isxcatalogviewer.core.data.repositories

import dev.mikhalchenkov.isxcatalogviewer.core.common.di.IoDispatcher
import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.CatalogDataSource
import dev.mikhalchenkov.isxcatalogviewer.core.data.mappers.CatalogItemDtoMapper.toDomain
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val catalogDataSource: CatalogDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CatalogRepository {
    override fun search(query: String): Flow<List<CatalogItem>> = flow {
        val all = catalogDataSource.getCatalog().items.map { it.toDomain() }
        val filtered = if (query.isBlank()) {
            all
        } else {
            all.filter { it.title.contains(query, ignoreCase = true) }
        }
        emit(filtered)
    }.flowOn(dispatcher)

    override suspend fun findById(id: String): CatalogItem? =
        catalogDataSource.getCatalog().items.firstOrNull { it.id == id }?.toDomain()
}
