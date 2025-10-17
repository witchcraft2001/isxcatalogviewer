package dev.mikhalchenkov.isxcatalogviewer.core.data.datasources

import android.content.Context
import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogResponseDto
import kotlinx.serialization.json.Json

class CatalogDataSourceImpl constructor(private val context: Context) : CatalogDataSource {
    companion object {
        private const val CATALOG_FILE_NAME = "catalog.json"
    }

    // todo: store in memory after first load
    override fun getCatalog(): CatalogResponseDto {
        val jsonString = context.assets.open(CATALOG_FILE_NAME).bufferedReader().use {
            it.readText()
        }
        return Json.decodeFromString<CatalogResponseDto>(jsonString)
    }
}