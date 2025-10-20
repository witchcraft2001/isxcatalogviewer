package dev.mikhalchenkov.isxcatalogviewer.core.data.datasources

import kotlinx.coroutines.flow.Flow

interface FavoritesDataSource {
    val favoriteIds: Flow<Set<String>>
    suspend fun toggle(id: String)
}