package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases

import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.toCatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class GetCatalogItemsUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(): Flow<Result<List<CatalogItemFavorite>>> =
        combine(
            catalogRepository.getAll(),
            favoriteRepository.favoriteIds
        ) { catalog, favoriteIds ->
            catalog.map { items ->
                items.map { item ->
                    item.toCatalogItemFavorite(favoriteIds.contains(item.id))
                }
            }
        }
}
