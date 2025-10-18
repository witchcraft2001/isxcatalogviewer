package dev.mikhalchenkov.isxcatalogviewer.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogResponseDto(
    @SerialName("items") val items: List<CatalogItemDto>,
    @SerialName("updatedAt") val updatedAt: String,
)
