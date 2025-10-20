package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.core.common.di.AsyncResult
import dev.mikhalchenkov.isxcatalogviewer.domain.usecases.ToggleFavoriteUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.domain.GetCatalogItemByIdUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.mappers.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CatalogDetailsViewModel @Inject constructor(
    private val getCatalogItemByIdUseCase: GetCatalogItemByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<CatalogDetailsState>(CatalogDetailsState.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<CatalogDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun loadItem(id: String) {
        viewModelScope.launch {
            getCatalogItemByIdUseCase(id).collect { result ->
                _state.value = when (result) {
                    is AsyncResult.Loading -> {
                        CatalogDetailsState.Loading
                    }

                    is AsyncResult.Success -> {
                        val item = result.value
                        if (item != null) {
                            CatalogDetailsState.Show(item.toUi())
                        } else {
                            CatalogDetailsState.NotFound
                        }
                    }

                    is AsyncResult.Error -> {
                        CatalogDetailsState.Error()
                    }
                }
            }
        }
    }

    fun onToggleFavorite(itemId: String) {
        // todo: add ResourceManager to viewmodel to get string from resources
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(itemId)
                _events.send(CatalogDetailEvent.ShowMessage("Favorite status updated"))
            } catch (e: Exception) {
                _events.send(
                    CatalogDetailEvent.ShowMessage(
                        e.message ?: "Unable to update favorite status"
                    )
                )
            }
        }
    }
}
