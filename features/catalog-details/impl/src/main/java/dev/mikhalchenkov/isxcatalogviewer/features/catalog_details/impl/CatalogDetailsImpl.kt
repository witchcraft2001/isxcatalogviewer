package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.api.CatalogDetailsApi
import javax.inject.Inject

class CatalogDetailsImpl @Inject constructor() : CatalogDetailsApi {
    @Composable
    override fun ScreenEntryPoint(
        id: String,
        onCloseDetails: () -> Unit
    ) {
        val viewModel = hiltViewModel<CatalogDetailsViewModel>()
        LaunchedEffect(Unit) {
            viewModel.loadItem()
        }
        CatalogDetailsScreen(
            state = viewModel.state.collectAsState().value,
            onCloseClicked = onCloseDetails,
        )
    }
}