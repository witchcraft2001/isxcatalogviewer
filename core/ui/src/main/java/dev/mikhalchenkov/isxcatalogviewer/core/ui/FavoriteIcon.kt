package dev.mikhalchenkov.isxcatalogviewer.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.theme.ISXCatalogViewerTheme

@Composable
fun FavoriteIcon(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(
            if (isFavorite) {
                R.drawable.ic_favorite_fill
            } else {
                R.drawable.ic_favorite_empty
            }
        ),
        contentDescription = null,
        modifier = modifier
            .size(24.dp)
            .clickable(
                onClick = onToggleFavorite,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
    )
}

@Preview
@Composable
private fun FavoriteIconPreview() {
    ISXCatalogViewerTheme {
        Column {
            FavoriteIcon(
                isFavorite = true,
                onToggleFavorite = {}
            )
            FavoriteIcon(
                isFavorite = false,
                onToggleFavorite = {}
            )
        }
    }
}