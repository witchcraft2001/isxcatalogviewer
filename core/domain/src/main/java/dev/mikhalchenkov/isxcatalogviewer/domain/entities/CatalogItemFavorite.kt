package dev.mikhalchenkov.isxcatalogviewer.domain.entities

data class CatalogItemFavorite(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val isFavorite: Boolean,
)

fun CatalogItem.toCatalogItemFavorite(isFavorite: Boolean) = CatalogItemFavorite(
    id = this.id,
    title = this.title,
    category = this.category,
    price = this.price,
    rating = this.rating,
    isFavorite = isFavorite
)
