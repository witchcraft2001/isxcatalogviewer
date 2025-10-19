package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.ErrorMessage
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.R
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation.compose.CatalogDetailsContentScreen
import dev.mikhalchenkov.isxcatalogviewer.core.ui.R as CoreUiR

@Composable
internal fun CatalogDetailsScreen(
    state: CatalogDetailsState,
    onCloseClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Details Screen",
                style = MaterialTheme.typography.headlineMedium,
            )
            Image(
                painter = painterResource(
                    CoreUiR.drawable.ic_close_circle
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onCloseClicked,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ),
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is CatalogDetailsState.Loading -> CircularProgressIndicator()
                is CatalogDetailsState.NotFound -> ErrorMessage(
                    message = stringResource(R.string.item_not_found_message),
                    buttonText = stringResource(R.string.close_btn),
                    onButtonClicked = onCloseClicked,
                )

                is CatalogDetailsState.Error -> ErrorMessage(
                    message = state.message
                        ?: stringResource(CoreUiR.string.unspecified_error_message),
                    onButtonClicked = onCloseClicked,
                )

                is CatalogDetailsState.Show -> CatalogDetailsContentScreen(
                    state = state,
                )
            }
        }
    }
}