package dev.mikhalchenkov.isxcatalogviewer.core.data.repositories

import dev.mikhalchenkov.isxcatalogviewer.core.data.datasources.FavoritesDataSource
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoritesDataSource: FavoritesDataSource
) : FavoriteRepository {
    override val favoriteIds = favoritesDataSource.favoriteIds

    override suspend fun toggle(id: String) = favoritesDataSource.toggle(id)
}