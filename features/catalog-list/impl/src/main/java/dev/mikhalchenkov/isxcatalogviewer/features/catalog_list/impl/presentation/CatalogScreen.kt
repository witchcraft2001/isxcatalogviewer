package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mikhalchenkov.isxcatalogviewer.core.ui.ErrorMessage
import dev.mikhalchenkov.isxcatalogviewer.core.ui.R as CoreUiR
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.compose.CatalogContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CatalogScreen(
    state: CatalogViewState,
    onReloadClicked: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenDetails: (String) -> Unit,
) {
    when (state) {
        is CatalogViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        is CatalogViewState.Error -> {
            ErrorMessage(
                message = state.message ?: stringResource(CoreUiR.string.unspecified_error_message),
                onButtonClicked = onReloadClicked,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        is CatalogViewState.Show -> {
            CatalogContent(
                state = state,
                onQueryChanged = onQueryChanged,
                onToggleFavorite = onToggleFavorite,
                onOpenDetails = onOpenDetails,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

