package dev.mikhalchenkov.isxcatalogviewer.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogItemDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("category") val category: String,
    @SerialName("price") val price: Double,
    @SerialName("rating") val rating: Double,
)
