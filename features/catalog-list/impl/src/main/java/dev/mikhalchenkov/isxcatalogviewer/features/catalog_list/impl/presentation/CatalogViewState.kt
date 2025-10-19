package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

sealed interface CatalogViewState {
    data object Loading : CatalogViewState
    data class Error(val message: String? = null) : CatalogViewState
    data class Show(val items: List<CatalogItemUi>, val query: String) : CatalogViewState
}
