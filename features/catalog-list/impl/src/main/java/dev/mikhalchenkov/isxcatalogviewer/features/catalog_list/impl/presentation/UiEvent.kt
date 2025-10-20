package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

internal sealed interface UiEvent {
    data class ShowMessage(val text: String) : UiEvent
}
