package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.mikhalchenkov.isxcatalogviewer.core.ui.FavoriteIcon

@Composable
internal fun Title(
    id: String,
    title: String,
    isFavorite: Boolean,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        FavoriteIcon(
            isFavorite = isFavorite,
            onToggleFavorite = { onToggleFavorite(id) }
        )
    }
}
