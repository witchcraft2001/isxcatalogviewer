package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.compose.CatalogContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    state: CatalogViewState,
    onQueryChanged: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenDetails: (String) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Catalog") }) }
    ) { paddingValues ->

            when (state) {
                is CatalogViewState.Loading -> {
                    // Show loading indicator
                }

                is CatalogViewState.Error -> {
                    // Show error message
                }

                is CatalogViewState.Show -> {
                    CatalogContent(
                        state = state,
                        onQueryChanged = onQueryChanged,
                        onToggleFavorite = onToggleFavorite,
                        onOpenDetails = onOpenDetails,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )
                }

        }
    }
}

