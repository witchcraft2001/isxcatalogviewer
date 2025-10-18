package dev.mikhalchenkov.isxcatalogviewer.core.data.datasources

import android.content.Context
import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogResponseDto
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogDataSource @Inject constructor(private val context: Context) {
    companion object {
        private const val CATALOG_FILE_NAME = "catalog.json"
    }

    private var cache: CatalogResponseDto? = null

    fun getCatalog() = cache ?: getCatalogFromAssets().also { cache = it }

    private fun getCatalogFromAssets(): CatalogResponseDto {
        val jsonString = context.assets.open(CATALOG_FILE_NAME).bufferedReader().use {
            it.readText()
        }
        return Json.decodeFromString<CatalogResponseDto>(jsonString)
    }
}