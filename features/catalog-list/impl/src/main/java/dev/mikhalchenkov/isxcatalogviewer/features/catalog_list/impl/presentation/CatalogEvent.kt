package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

internal sealed interface CatalogEvent {
    data class ShowMessage(val text: String) : CatalogEvent
}
