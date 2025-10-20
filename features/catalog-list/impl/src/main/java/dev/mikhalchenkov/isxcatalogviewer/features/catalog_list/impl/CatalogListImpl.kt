package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api.CatalogListApi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogScreen
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogViewModel
import javax.inject.Inject

class CatalogListImpl @Inject constructor() : CatalogListApi {
    @Composable
    override fun ScreenEntryPoint(
        onOpenDetails: (String) -> Unit,
    ) {
        val viewModel = hiltViewModel<CatalogViewModel>()
        CatalogScreen(
            state = viewModel.state.collectAsState().value,
            onQueryChanged = viewModel::onQueryChanged,
            onToggleFavorite = viewModel::onToggleFavorite,
            onOpenDetails = onOpenDetails,
            onReloadClicked = viewModel::retry,
        )
    }
}