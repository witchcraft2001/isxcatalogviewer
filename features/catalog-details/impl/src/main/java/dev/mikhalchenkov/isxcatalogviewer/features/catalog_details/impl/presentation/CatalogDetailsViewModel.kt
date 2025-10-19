package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.domain.usecases.ToggleFavoriteUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.domain.GetCatalogItemByIdUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.mappers.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CatalogDetailsViewModel @Inject constructor(
    private val getCatalogItemByIdUseCase: GetCatalogItemByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<CatalogDetailsState>(CatalogDetailsState.Loading)
    val state = _state.asStateFlow()

    fun loadItem(id: String) {
        viewModelScope.launch {
            getCatalogItemByIdUseCase(id).collect { result ->
                _state.value = result.fold(
                    onSuccess = { item ->
                        if (item != null) {
                            CatalogDetailsState.Show(item.toUi())
                        } else {
                            CatalogDetailsState.NotFound
                        }
                    },
                    onFailure = { error ->
                        CatalogDetailsState.Error()
                    }
                )
            }
        }
    }

    fun onToggleFavorite(itemId: String) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(itemId)
            } catch (e: Exception) {
                _state.value = CatalogDetailsState.Error()
            }
        }
    }
}
