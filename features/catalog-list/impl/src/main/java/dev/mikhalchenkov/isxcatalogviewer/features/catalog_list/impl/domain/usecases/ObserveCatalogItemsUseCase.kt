package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases

import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.toCatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.CatalogRepository
import dev.mikhalchenkov.isxcatalogviewer.domain.repositories.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class ObserveCatalogItemsUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(refreshTrigger: Flow<Unit>): Flow<AsyncResult<List<CatalogItemFavorite>>> =
        refreshTrigger
            .onStart { emit(Unit) }
            .flatMapLatest {
                catalogRepository.getAll()
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
