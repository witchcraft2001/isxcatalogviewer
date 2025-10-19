package dev.mikhalchenkov.isxcatalogviewer.features.catalog_details.impl.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class CatalogDetailsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<CatalogDetailsState>(CatalogDetailsState.Loading)
    val state = _state.asStateFlow()

    fun loadItem() {

    }

}
