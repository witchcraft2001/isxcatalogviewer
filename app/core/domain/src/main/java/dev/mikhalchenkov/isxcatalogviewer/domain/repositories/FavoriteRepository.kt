package dev.mikhalchenkov.isxcatalogviewer.domain.repositories

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    val favoriteIds: Flow<Set<String>>
    suspend fun toggle(id: String)
}