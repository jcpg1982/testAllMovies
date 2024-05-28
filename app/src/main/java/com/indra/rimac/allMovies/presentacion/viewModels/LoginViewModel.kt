package com.indra.rimac.allMovies.presentacion.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.models.User
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventLogin
import com.indra.rimac.allMovies.utils.helpers.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _loginSuccessful: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val loginSuccessful = _loginSuccessful.asStateFlow()

    private val _enabledBottom: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val enabledBottom = _enabledBottom.asStateFlow()

    private val validateLogin: Boolean
        get() = user.value.user == Constants.Credentials.USERNAME && user.value.password == Constants.Credentials.PASSWORD

    fun handleEvent(event: UiEventLogin) {
        when (event) {
            UiEventLogin.Login -> login()
            is UiEventLogin.UpdateDataUser -> {
                _user.update { u ->
                    u.copy(user = event.user, password = event.password)
                }
                _enabledBottom.value =
                    !this.user.value.user.isNullOrEmpty() && !this.user.value.password.isNullOrEmpty()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _loginSuccessful.value = UiState.Loading(true, "Iniciando sesión")
            delay(3000)
            if (validateLogin) _loginSuccessful.value = UiState.Success(true)
            else  _loginSuccessful.value = UiState.NotSuccess(Throwable("Verificar sus datos"))
        }
    }
}