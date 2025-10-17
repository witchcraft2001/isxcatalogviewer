package dev.mikhalchenkov.isxcatalogviewer.core.data.mappers

import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogItemDto
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItem

object CatalogItemDtoMapper {
    fun CatalogItemDto.toDomain() = CatalogItem(
        id = this.id,
        title = this.title,
        category = this.category,
        price = this.price,
        rating = this.rating,
    )
}