package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.mappers

import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogItemUi

// This is simple mapper function, but we can add more complex
// logic here if needed, e.g. localization, price formatting, etc.
internal fun CatalogItemFavorite.toUi() = CatalogItemUi(
    id = this.id,
    title = this.title,
    category = this.category,
    price = "$%.2f".format(this.price),
    rating = "%.1f".format(this.rating),
    isFavorite = this.isFavorite,
)