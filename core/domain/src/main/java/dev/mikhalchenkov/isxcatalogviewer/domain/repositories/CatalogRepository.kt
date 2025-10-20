package dev.mikhalchenkov.isxcatalogviewer.domain.repositories

import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    fun getAll(): Flow<List<CatalogItem>>
    suspend fun findById(id: String): CatalogItem?
}