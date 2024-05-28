package com.indra.rimac.allMovies.presentacion.interfaces

sealed interface UiEventLogin {
    data object Login : UiEventLogin
    data class UpdateDataUser(val user: String, val password: String) : UiEventLogin
}
