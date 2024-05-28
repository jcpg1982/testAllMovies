package com.indra.rimac.allMovies.data.state

sealed class UiState<out T> {

    data class Loading(val isLoading: Boolean, val message: String) : UiState<Nothing>()

    data class NotSuccess(val throwable: Throwable) : UiState<Nothing>()

    data class Success<out R>(val data: R) : UiState<R>()
}
