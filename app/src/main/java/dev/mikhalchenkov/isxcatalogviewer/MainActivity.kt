package dev.mikhalchenkov.isxcatalogviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.mikhalchenkov.isxcatalogviewer.core.ui.theme.ISXCatalogViewerTheme
import dev.mikhalchenkov.isxcatalogviewer.features.home_screen.HomeApi
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var homeFeature: HomeApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISXCatalogViewerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    homeFeature.ScreenEntryPoint()
                }
            }
        }
    }
}
