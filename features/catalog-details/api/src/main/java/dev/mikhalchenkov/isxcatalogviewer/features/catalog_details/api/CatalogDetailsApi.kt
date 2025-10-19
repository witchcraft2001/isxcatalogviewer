package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.api

import androidx.compose.runtime.Composable

interface CatalogDetailsApi {
    @Composable
    fun ScreenEntryPoint(
        id: String,
        onCloseDetails: () -> Unit,
    )
}