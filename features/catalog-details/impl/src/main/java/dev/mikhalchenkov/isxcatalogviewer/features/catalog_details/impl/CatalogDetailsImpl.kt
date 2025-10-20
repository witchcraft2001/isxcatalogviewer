package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.core.ui.LocalAppSnackbarHostState
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.api.CatalogDetailsApi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogDetailEvent
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogDetailsScreen
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogDetailsViewModel
import javax.inject.Inject

class CatalogDetailsImpl @Inject constructor() : CatalogDetailsApi {
    @Composable
    override fun ScreenEntryPoint(
        id: String,
        onCloseDetails: () -> Unit
    ) {
        val viewModel = hiltViewModel<CatalogDetailsViewModel>()
        LaunchedEffect(id) {
            viewModel.loadItem(id)
        }

        val snackbarHostState: SnackbarHostState? = LocalAppSnackbarHostState.current

        LaunchedEffect(Unit) {
            if (snackbarHostState == null) return@LaunchedEffect
            viewModel.events.collect { event ->
                when (event) {
                    is CatalogDetailEvent.ShowMessage -> snackbarHostState.showSnackbar(event.text)
                }
            }
        }
        CatalogDetailsScreen(
            state = viewModel.state.collectAsState().value,
            onCloseClicked = onCloseDetails,
            onToggleFavorite = viewModel::onToggleFavorite,
        )
    }
}
