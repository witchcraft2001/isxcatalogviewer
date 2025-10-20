package dev.mikhalchenkov.isxcatalogviewer.features.home.impl.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    fun setId(id: String) {
        _state.value = _state.value.copy(id = id)
    }

    fun clearId() {
        _state.value = _state.value.copy(id = null)
    }
}
