package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogViewState


@Composable
internal fun ColumnScope.CatalogListNotEmpty(
    state: CatalogViewState.Show,
    onToggleFavorite: (String) -> Unit,
    onOpenDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = state.items,
            key = { it.id },
            contentType = { index -> "catalog_item" },
        ) { item ->
            CatalogItem(
                item = item,
                onToggleFavorite = onToggleFavorite,
                onOpenDetails = onOpenDetails,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}
