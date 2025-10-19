package dev.mikhalchenkov.isxcatalogviewer.features.home.impl.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api.CatalogListApi
import dev.mikhalchenkov.isxcatalogviewer.features.home.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    catalogFeature: CatalogListApi,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.application_name)) }) }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val configuration = LocalConfiguration.current

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                PortraitLayout(
                    catalogFeature = catalogFeature,
                    onOpenDetails = {},
                    modifier = modifier
                )
            }

            else -> {
                LandscapeLayout(
                    catalogFeature = catalogFeature,
                    onOpenDetails = {},
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun PortraitLayout(
    catalogFeature: CatalogListApi,
    onOpenDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            catalogFeature.ScreenEntryPoint(
                onOpenDetails = onOpenDetails,
            )
        }
    }
}

@Composable
fun LandscapeLayout(
    catalogFeature: CatalogListApi,
    onOpenDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            catalogFeature.ScreenEntryPoint(
                onOpenDetails = onOpenDetails,
            )
        }
    }
}
