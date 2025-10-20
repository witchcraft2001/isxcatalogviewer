package dev.mikhalchenkov.isxcatalogviewer.core.data.datasources

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mikhalchenkov.isxcatalogviewer.core.common.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "catalog_preferences")

@Singleton
class FavoritesDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FavoritesDataSource {

    companion object {
        private val KEY: Preferences.Key<Set<String>> =
            stringSetPreferencesKey(name = "favorite_ids")
    }

    override val favoriteIds: Flow<Set<String>>
        get() = context.dataStore.data.map { it[KEY] ?: emptySet() }

    override suspend fun toggle(id: String) {
        withContext(dispatcher) {
            context.dataStore.edit { preferences ->
                val current = preferences[KEY] ?: emptySet()
                if (current.contains(id)) {
                    preferences[KEY] = current - id
                } else {
                    preferences[KEY] = current + id
                }
            }
        }
    }
}
