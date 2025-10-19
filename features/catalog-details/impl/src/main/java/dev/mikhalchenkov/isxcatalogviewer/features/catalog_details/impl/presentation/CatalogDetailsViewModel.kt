package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.domain.GetCatalogItemByIdUseCase
import dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.mappers.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CatalogDetailsViewModel @Inject constructor(
    private val getCatalogItemByIdUseCase: GetCatalogItemByIdUseCase
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
                        CatalogDetailsState.Error(error.message)
                    }
                )
            }
        }
    }
}
