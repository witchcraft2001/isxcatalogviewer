package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mikhalchenkov.isxcatalogviewer.core.ui.LocalAppSnackbarHostState
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api.CatalogListApi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogScreen
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogViewModel
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogEvent
import javax.inject.Inject

class CatalogListImpl @Inject constructor() : CatalogListApi {
    @Composable
    override fun ScreenEntryPoint(
        onOpenDetails: (String) -> Unit,
    ) {
        val viewModel = hiltViewModel<CatalogViewModel>()
        val snackbarHostState: SnackbarHostState? = LocalAppSnackbarHostState.current

        LaunchedEffect(Unit) {
            if (snackbarHostState == null) return@LaunchedEffect
            viewModel.events.collect { event ->
                when (event) {
                    is CatalogEvent.ShowMessage -> snackbarHostState.showSnackbar(event.text)
                }
            }
        }

        CatalogScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onQueryChanged = viewModel::onQueryChanged,
            onToggleFavorite = viewModel::onToggleFavorite,
            onOpenDetails = onOpenDetails,
            onReloadClicked = viewModel::retry,
        )
    }
}
