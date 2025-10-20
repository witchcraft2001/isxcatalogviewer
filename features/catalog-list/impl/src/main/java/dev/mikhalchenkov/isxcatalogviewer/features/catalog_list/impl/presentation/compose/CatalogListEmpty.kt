package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.ErrorMessage
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.R

@Composable
internal fun ColumnScope.CatalogListEmpty(
    onResetFilters: () -> Unit,
) {
    ErrorMessage(
        modifier = Modifier.weight(1f).padding(16.dp),
        message = stringResource(R.string.nothing_to_show_message),
        buttonText = stringResource(R.string.reset_filters_btn),
        onButtonClicked = onResetFilters,
    )
}
