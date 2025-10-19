package dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.domain.entities.CatalogItemFavorite
import dev.mikhalchenkov.isxcatalogviewer.domain.usecases.ToggleFavoriteUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.domain.usecases.GetCatalogItemsUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_list.impl.mappers.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CatalogViewModel @Inject constructor(
    private val getCatalogItemsUseCase: GetCatalogItemsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val catalogResult =
        MutableStateFlow<Result<List<CatalogItemFavorite>>>(Result.success(emptyList()))

    private val _state = MutableStateFlow<CatalogViewState>(CatalogViewState.Loading)
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            combine(catalogResult, query) { result, query ->
                result.fold(
                    onSuccess = { items ->
                        val filtered = if (query.isBlank()) {
                            items
                        } else {
                            items.filter {
                                it.title.contains(query, ignoreCase = true)
                            }
                        }
                        _state.value = CatalogViewState.Show(filtered.map { it.toUi() }, query)
                    },
                    onFailure = { throwable ->
                        _state.value = CatalogViewState.Error()
                    }
                )
            }.stateIn(viewModelScope, SharingStarted.Eagerly, Unit)
        }
    }

    fun loadCatalog() {
        _state.value = CatalogViewState.Loading
        viewModelScope.launch {
            getCatalogItemsUseCase().collect { result ->
                catalogResult.value = result
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun onToggleFavorite(itemId: String) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(itemId)
            } catch (e: Exception) {
                _state.value = CatalogViewState.Error(e.message)
            }
        }
    }
}