package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.domain

import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.toCatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class GetCatalogItemByIdUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(id: String): Flow<AsyncResult<CatalogItemFavorite?>> =
        combine(
            getFlowCatalogItemById(id),
            favoriteRepository.favoriteIds
        ) { item, favoriteIds ->
            item?.toCatalogItemFavorite(favoriteIds.contains(id))
        }
            .map<_, AsyncResult<CatalogItemFavorite?>> { AsyncResult.Success(it) }
            .onStart { emit(AsyncResult.Loading) }
            .catch { emit(AsyncResult.Error(it)) }

    private fun getFlowCatalogItemById(id: String) = flow {
        emit(catalogRepository.findById(id))
    }
}
