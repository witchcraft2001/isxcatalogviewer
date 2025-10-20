package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.usecases.ToggleFavoriteUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases.ObserveCatalogItemsUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.mappers.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CatalogViewModel @Inject constructor(
    private val getCatalogItemsUseCase: ObserveCatalogItemsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val refresh = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1).apply {
        tryEmit(Unit)
    }

    private val query = MutableStateFlow("")

    private val itemsAsync: StateFlow<AsyncResult<List<CatalogItemFavorite>>> =
        getCatalogItemsUseCase(refresh)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AsyncResult.Loading)

    val state: StateFlow<CatalogViewState> =
        combine(itemsAsync, query) { async, q ->
            when (async) {
                is AsyncResult.Loading -> CatalogViewState.Loading
                is AsyncResult.Error -> CatalogViewState.Error(message = async.throwable.message)
                is AsyncResult.Success -> {
                    val filtered = if (q.isBlank()) async.value
                    else async.value.filter { it.title.contains(q, ignoreCase = true) }
                    CatalogViewState.Show(filtered.map { it.toUi() }, q)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CatalogViewState.Loading)

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun retry() {
        refresh.tryEmit(Unit)
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun onToggleFavorite(itemId: String) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(itemId)
                _events.send(UiEvent.ShowMessage("Favorite status updated"))
            } catch (e: Exception) {
                // todo: add ResourceManager to viewmodel to get string from resources
                _events.send(UiEvent.ShowMessage(e.message ?: "Unable to update favorite status"))
            }
        }
    }
}