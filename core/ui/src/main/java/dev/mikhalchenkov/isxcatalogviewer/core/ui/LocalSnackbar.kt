package dev.mikhalchenkov.isxcatalogviewer.core.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppSnackbarHostState = staticCompositionLocalOf<SnackbarHostState?> { null }