package dev.mikhalchenkov.isxcatalogviewer.features.home.impl.presentation

import androidx.compose.runtime.Composable
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api.CatalogListApi

@Composable
internal fun HomeScreen(
    catalogFeature: CatalogListApi,
) {
    catalogFeature.ScreenEntryPoint(
        onOpenDetails = { /* TODO */ },
    )
}