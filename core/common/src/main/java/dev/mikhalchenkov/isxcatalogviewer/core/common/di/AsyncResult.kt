package dev.mikhalchenkov.isxcatalogviewer.core.common.di

sealed interface AsyncResult<out T> {
    data object Loading : AsyncResult<Nothing>
    data class Success<T>(val value: T) : AsyncResult<T>
    data class Error(val throwable: Throwable) : AsyncResult<Nothing>
}
