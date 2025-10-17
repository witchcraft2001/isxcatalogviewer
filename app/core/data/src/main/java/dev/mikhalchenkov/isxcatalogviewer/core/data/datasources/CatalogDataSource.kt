package dev.mikhalchenkov.isxcatalogviewer.core.data.datasources

import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogResponseDto

interface CatalogDataSource {
    fun getCatalog(): CatalogResponseDto
}