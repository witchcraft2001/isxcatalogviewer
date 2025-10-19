package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.Rating
import dev.mikhalchenkov.isxcatalogviewer.core.ui.theme.ISXCatalogViewerTheme
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogDetailsState

@Composable
internal fun CatalogDetailsContentScreen(
    state: CatalogDetailsState.Show,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        with(state.item) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(category, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Rating(rating)
            }
            Text(price, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Preview
@Composable
private fun CatalogDetailsContentScreenPreview() {
    ISXCatalogViewerTheme {
        CatalogDetailsContentScreen(
            state = CatalogDetailsState.Show(
                item = dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.CatalogItemUi(
                    id = "1",
                    title = "Sample Item",
                    category = "Sample Category",
                    price = "$9.99",
                    rating = "5.0",
                    isFavorite = false,
                )
            )
        )
    }
}