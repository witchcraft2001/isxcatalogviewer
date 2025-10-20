package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

import androidx.compose.runtime.Immutable

internal sealed interface CatalogViewState {
    data object Loading : CatalogViewState
    data class Error(val message: String? = null) : CatalogViewState
    @Immutable
    data class Show(val items: List<CatalogItemUi>, val query: String) : CatalogViewState
}
