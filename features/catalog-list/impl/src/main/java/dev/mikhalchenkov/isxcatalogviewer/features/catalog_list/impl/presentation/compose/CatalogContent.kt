package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.theme.ISXCatalogViewerTheme
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogItemUi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogViewState

@Composable
internal fun CatalogContent(
    state: CatalogViewState.Show,
    onQueryChanged: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )

        if (state.items.isEmpty()) {
            CatalogListEmpty(
                onResetFilters = { onQueryChanged("") }
            )
        } else {
            CatalogListNotEmpty(
                state = state,
                onToggleFavorite = onToggleFavorite,
                onOpenDetails = onOpenDetails,
            )
        }
    }
}

@Preview
@Composable
private fun CatalogItemPreview() {
    val items = listOf(
        CatalogItemUi(
            id = "1",
            title = "Sample Item Title 1",
            category = "Sample Category 1",
            isFavorite = true,
            price = "$9.99",
            rating = "4.5",
        ),
        CatalogItemUi(
            id = "2",
            title = "Sample Item Title 2",
            category = "Sample Category 2",
            isFavorite = false,
            price = "$5.99",
            rating = "4.2",
        )
    )
    ISXCatalogViewerTheme {
        Column {
            CatalogContent(
                state = CatalogViewState.Show(
                    query = "",
                    items = items,
                ),
                onQueryChanged = {},
                onToggleFavorite = {},
                onOpenDetails = {},
            )
        }
    }
}
