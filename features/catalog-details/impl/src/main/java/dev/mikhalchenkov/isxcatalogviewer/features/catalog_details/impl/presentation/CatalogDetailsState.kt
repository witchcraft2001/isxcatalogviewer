package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation

internal sealed interface CatalogDetailsState {
    data object Loading : CatalogDetailsState
    data class Error(val message: String? = null) : CatalogDetailsState
    data class Show(val item: CatalogItemUi) : CatalogDetailsState
}
