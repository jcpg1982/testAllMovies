package com.indra.rimac.allMovies.presentacion.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.indra.rimac.allMovies.R
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.models.Routes
import com.indra.rimac.allMovies.presentacion.dialog.DialogAlert
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventLogin
import com.indra.rimac.allMovies.presentacion.theme.ColorWhite
import com.indra.rimac.allMovies.presentacion.theme.Purple80
import com.indra.rimac.allMovies.presentacion.theme.contentInset
import com.indra.rimac.allMovies.presentacion.theme.contentInsetThirtyTwo
import com.indra.rimac.allMovies.presentacion.ui.composables.CustomButton
import com.indra.rimac.allMovies.presentacion.ui.composables.CustomTextInput
import com.indra.rimac.allMovies.presentacion.ui.composables.CustomTextPassword
import com.indra.rimac.allMovies.presentacion.ui.composables.LoadingData
import com.indra.rimac.allMovies.presentacion.viewModels.LoginViewModel
import com.indra.rimac.allMovies.presentacion.viewModels.NavigationViewModel
import com.indra.rimac.allMovies.utils.helpers.Constants

@Composable
fun Login(navigationViewModel: NavigationViewModel) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val user by loginViewModel.user.collectAsState()
    val enabledBottom by loginViewModel.enabledBottom.collectAsState()
    val loginSuccessful by loginViewModel.loginSuccessful.collectAsState()

    var messageLoading by rememberSaveable { mutableStateOf(value = "") }
    var messageError by rememberSaveable { mutableStateOf(value = "") }

    LaunchedEffect(loginSuccessful) {
        when (val state = loginSuccessful) {
            is UiState.Loading -> if (state.isLoading) messageLoading = state.message
            is UiState.NotSuccess -> {
                messageLoading = ""
                messageError = state.throwable.message.orEmpty()
            }

            is UiState.Success -> {
                messageLoading = ""
                messageError = ""
                navigationViewModel.onNavigateTo(Routes.Home)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.header),
            contentDescription = "header",
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = contentInset, end = contentInset, top = contentInsetThirtyTwo
                )
                .align(Alignment.CenterHorizontally),
            alignment = Alignment.TopCenter
        )

        CustomTextInput(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = contentInset, end = contentInset, top = contentInset
            ),
            hintText = "Usuario",
            value = user.user.orEmpty(),
            maxCharacter = 20,
            isEnabled = true,
            isReadOnly = false,
            keyboardType = KeyboardType.Text,
            isErrorDisplayed = false,
            messageError = "",
            maxLines = 1,
            regex = Constants.Regex.ONLY_LETTERS,
            onTextValueChange = {
                loginViewModel.handleEvent(UiEventLogin.UpdateDataUser(it, user.password.orEmpty()))
            },
            onClickTextView = {})

        CustomTextPassword(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = contentInset, end = contentInset, top = contentInset
            ),
            hintPassword = "Contraseña",
            password = user.password.orEmpty(),
            maxCharacter = 20,
            isErrorDisplayed = false,
            messageError = "",
            regex = Constants.Regex.MIXTO,
            onValueChange = {
                loginViewModel.handleEvent(UiEventLogin.UpdateDataUser(user.user.orEmpty(), it))
            })

        CustomButton(modifier = Modifier
            .fillMaxWidth()
            .padding(all = contentInset),
            enabledButton = enabledBottom,
            textButton = "Iniciar sesión",
            containerColor = Purple80,
            onAcceptButton = { loginViewModel.handleEvent(UiEventLogin.Login) })
    }

    if (messageLoading.isNotEmpty()) LoadingData(message = messageLoading)
    if (messageError.isNotEmpty()) {
        DialogAlert(title = "Error",
            message = messageError,
            isCancelable = true,
            textPositiveButton = "OK",
            textColorPositiveButton = ColorWhite,
            backgroundColorPositiveButton = Purple80,
            onPositiveCallback = { messageError = "" },
            onNegativeCallback = { messageError = "" },
            onDismissDialog = { messageError = "" })
    }
}