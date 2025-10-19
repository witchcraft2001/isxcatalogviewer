package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.domain

import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.toCatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCatalogItemByIdUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(id: String): Flow<Result<CatalogItemFavorite?>> =
        combine(
            getFlowCatalogItemById(id),
            favoriteRepository.favoriteIds
        ) { item, favoriteIds ->
            item.map { catalogItem ->
                catalogItem?.toCatalogItemFavorite(favoriteIds.contains(id))
            }
        }

    private fun getFlowCatalogItemById(id: String) = flow {
        emit(catalogRepository.findById(id))
    }
}
