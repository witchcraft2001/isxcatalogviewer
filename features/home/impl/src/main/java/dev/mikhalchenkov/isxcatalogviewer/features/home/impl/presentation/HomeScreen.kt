package dev.mikhalchenkov.isxcatalogviewer.features.home.impl.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.api.CatalogDetailsApi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api.CatalogListApi
import dev.mikhalchenkov.isxcatalogviewer.features.home.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    catalogFeature: CatalogListApi,
    catalogDetailsApi: CatalogDetailsApi,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.application_name)) }) }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val configuration = LocalConfiguration.current
        var id by remember { mutableStateOf<String?>(null) }

        val onOpenDetails: (String) -> Unit = remember { { selectedId -> id = selectedId } }
        val onCloseDetails: () -> Unit = remember { { id = null } }

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                PortraitLayout(
                    id = id,
                    catalogFeature = catalogFeature,
                    catalogDetailsApi = catalogDetailsApi,
                    onOpenDetails = onOpenDetails,
                    onCloseDetails = onCloseDetails,
                    modifier = modifier
                )
            }

            else -> {
                LandscapeLayout(
                    id = id,
                    catalogFeature = catalogFeature,
                    catalogDetailsApi = catalogDetailsApi,
                    onOpenDetails = onOpenDetails,
                    onCloseDetails = onCloseDetails,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun PortraitLayout(
    id: String?,
    catalogFeature: CatalogListApi,
    catalogDetailsApi: CatalogDetailsApi,
    onOpenDetails: (String) -> Unit,
    onCloseDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            catalogFeature.ScreenEntryPoint(
                onOpenDetails = onOpenDetails,
            )
        }
        if (id != null) {
            HorizontalDivider()
        }
        DetailsLayout(
            id = id,
            catalogDetailsApi = catalogDetailsApi,
            onCloseDetails = onCloseDetails,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun LandscapeLayout(
    id: String?,
    catalogFeature: CatalogListApi,
    catalogDetailsApi: CatalogDetailsApi,
    onOpenDetails: (String) -> Unit,
    onCloseDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            catalogFeature.ScreenEntryPoint(
                onOpenDetails = onOpenDetails,
            )
        }
        if (id != null) {
            VerticalDivider()
        }
        DetailsLayout(
            id = id,
            catalogDetailsApi = catalogDetailsApi,
            onCloseDetails = onCloseDetails,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DetailsLayout(
    id: String?,
    catalogDetailsApi: CatalogDetailsApi,
    onCloseDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (id != null) {
        Box(
            modifier = modifier,
        ) {
            catalogDetailsApi.ScreenEntryPoint(
                id = id,
                onCloseDetails = onCloseDetails,
            )
        }
    }
}
