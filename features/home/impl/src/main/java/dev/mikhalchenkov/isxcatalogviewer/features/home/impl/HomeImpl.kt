package dev.mikhalchenkov.isxcatalogviewer.features.home.impl

import androidx.compose.runtime.Composable
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.api.CatalogDetailsApi
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.api.CatalogListApi
import dev.mikhalchenkov.isxcatalogviewer.features.home.impl.presentation.HomeScreen
import dev.mikhalchenkov.isxcatalogviewer.features.home_screen.HomeApi
import javax.inject.Inject

class HomeImpl @Inject constructor(
    private val catalogListApi: CatalogListApi,
    private val catalogDetailsApi: CatalogDetailsApi,
) : HomeApi {
    @Composable
    override fun ScreenEntryPoint() {
        HomeScreen(
            catalogFeature = catalogListApi,
            catalogDetailsApi = catalogDetailsApi,
        )
    }
}
