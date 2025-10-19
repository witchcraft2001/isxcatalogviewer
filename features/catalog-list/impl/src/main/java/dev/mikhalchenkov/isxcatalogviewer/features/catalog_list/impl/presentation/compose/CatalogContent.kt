package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.FavoriteIcon
import dev.mikhalchenkov.isxcatalogviewer.core.ui.Rating
import dev.mikhalchenkov.isxcatalogviewer.core.ui.theme.ISXCatalogViewerTheme
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogItemUi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogViewState
import dev.mikhalchenkov.isxcatalogviewer.core.ui.R as CoreUiR

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

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                count = state.items.size,
                key = { index -> state.items[index].id },
                contentType = { index -> "catalog_item" },
            ) {
                CatalogItem(
                    item = state.items[it],
                    onToggleFavorite = onToggleFavorite,
                    onOpenDetails = onOpenDetails,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun CatalogItem(
    modifier: Modifier = Modifier,
    onToggleFavorite: (String) -> Unit,
    onOpenDetails: (String) -> Unit,
    item: CatalogItemUi,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenDetails(item.id) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = item.title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = item.category,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                FavoriteIcon(
                    isFavorite = item.isFavorite,
                    onToggleFavorite = { onToggleFavorite(item.id) },
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Rating(rating = item.rating)
                Text(
                    text = item.price,
                    style = MaterialTheme.typography.titleLarge
                )
            }
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