package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation

internal sealed interface CatalogDetailEvent {
    data class ShowMessage(val text: String) : CatalogDetailEvent
}
