package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

data class CatalogItemUi(
    val id: String,
    val title: String,
    val category: String,
    val price: String,
    val rating: String,
    val isFavorite: Boolean,
)
