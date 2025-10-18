package dev.mikhalchenkov.isxcatalogviewer.domain.entities

data class CatalogItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val rating: Double,
)
