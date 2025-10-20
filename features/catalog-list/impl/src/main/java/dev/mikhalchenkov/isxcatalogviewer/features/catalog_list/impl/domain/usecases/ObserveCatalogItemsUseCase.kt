package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases

import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.toCatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class ObserveCatalogItemsUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(
        queryFlow: Flow<String>,
        refreshTrigger: Flow<Unit>,
        debounceMs: Long = 300L,
    ): Flow<AsyncResult<List<CatalogItemFavorite>>> =
        combine(
            queryFlow
                .map { it.trim() }
                .debounce(debounceMs)
                .distinctUntilChanged(),
            refreshTrigger.onStart { emit(Unit) }
        ) { query, _ -> query }
            .flatMapLatest { query ->
                catalogRepository.search(query)
                    .combine(favoriteRepository.favoriteIds) { items, favoriteIds ->
                        items.map { item ->
                            item.toCatalogItemFavorite(favoriteIds.contains(item.id))
                        }
                    }
                    .map<_, AsyncResult<List<CatalogItemFavorite>>> { AsyncResult.Success(it) }
                    .onStart { emit(AsyncResult.Loading) }
                    .catch { emit(AsyncResult.Error(it)) }
            }
}
