package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.FavoriteIcon
import dev.mikhalchenkov.isxcatalogviewer.core.ui.Rating
import dev.mikhalchenkov.isxcatalogviewer.core.ui.theme.ISXCatalogViewerTheme
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.CatalogItemUi

@Composable
internal fun CatalogItem(
    modifier: Modifier = Modifier,
    onToggleFavorite: (String) -> Unit,
    onOpenDetails: (String) -> Unit,
    item: CatalogItemUi,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .clickable { onOpenDetails(item.id) }
                .padding(16.dp)
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
    ISXCatalogViewerTheme {
        Column {
            CatalogItem(
                item = CatalogItemUi(
                    id = "1",
                    title = "Sample Item Title 1",
                    category = "Sample Category 1",
                    isFavorite = true,
                    price = "$9.99",
                    rating = "4.5",
                ),
                onToggleFavorite = {},
                onOpenDetails = {}
            )
        }
    }
}
