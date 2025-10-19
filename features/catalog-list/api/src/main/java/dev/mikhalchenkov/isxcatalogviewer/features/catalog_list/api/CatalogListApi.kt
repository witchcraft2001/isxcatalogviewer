package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api

import androidx.compose.runtime.Composable

interface CatalogListApi {
    @Composable
    fun ScreenEntryPoint(
        onOpenDetails: (String) -> Unit,
    )
}