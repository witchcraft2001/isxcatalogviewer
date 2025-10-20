package dev.mikhalchenkov.isxcatalogviewer.core.data.datasources

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mikhalchenkov.isxcatalogviewer.core.common.di.IoDispatcher
import dev.mikhalchenkov.isxcatalogviewer.core.data.models.CatalogResponseDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CatalogDataSource {
    companion object {
        private const val CATALOG_FILE_NAME = "catalog.json"
    }

    private var cache: CatalogResponseDto? = null
    private val mutex = Mutex()

    override suspend fun getCatalog() = withContext(dispatcher) {
        mutex.withLock {
            cache ?: getCatalogFromAssets().also { cache = it }
        }
    }

    private fun getCatalogFromAssets(): CatalogResponseDto {
        val jsonString = context.assets.open(CATALOG_FILE_NAME).bufferedReader().use {
            it.readText()
        }
        return Json.decodeFromString<CatalogResponseDto>(jsonString)
    }
}
