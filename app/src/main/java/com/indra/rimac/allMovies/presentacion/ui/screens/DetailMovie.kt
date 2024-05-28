package com.indra.rimac.allMovies.presentacion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.models.Routes
import com.indra.rimac.allMovies.presentacion.dialog.DialogAlert
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventDetail
import com.indra.rimac.allMovies.presentacion.theme.ColorWhite
import com.indra.rimac.allMovies.presentacion.theme.Purple80
import com.indra.rimac.allMovies.presentacion.theme.contentInset
import com.indra.rimac.allMovies.presentacion.theme.contentInsetEight
import com.indra.rimac.allMovies.presentacion.theme.contentInsetQuarter
import com.indra.rimac.allMovies.presentacion.theme.dynamicDisplayTextSize
import com.indra.rimac.allMovies.presentacion.theme.dynamicTwentyFourTextSize
import com.indra.rimac.allMovies.presentacion.ui.composables.ImageSubComposeAsyncImage
import com.indra.rimac.allMovies.presentacion.ui.composables.LoadingData
import com.indra.rimac.allMovies.presentacion.viewModels.DetailViewModel
import com.indra.rimac.allMovies.utils.helpers.Constants

@Composable
fun DetailMovie(movieId: Int) {

    val detailViewModel: DetailViewModel = hiltViewModel()
    val getItemMovie by detailViewModel.getItemMovie.collectAsState()
    val movieWithGenreId by detailViewModel.movieWithGenreId.collectAsState()

    var messageLoading by rememberSaveable { mutableStateOf(value = "") }
    var messageError by rememberSaveable { mutableStateOf(value = "") }

    LaunchedEffect(movieId) {
        detailViewModel.handleEvent(UiEventDetail.ObtainDetailMovie(movieId))
    }

    LaunchedEffect(getItemMovie) {
        when (val state = getItemMovie) {
            is UiState.Loading -> if (state.isLoading) messageLoading = state.message
            is UiState.NotSuccess -> {
                messageLoading = ""
                messageError = state.throwable.message.orEmpty()
            }

            is UiState.Success -> {
                messageLoading = ""
                messageError = ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ImageSubComposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            imageUrl = "${Constants.URL_IMAGE}${movieWithGenreId.movie.posterPath}",
            contentScale = ContentScale.FillBounds,
            description = movieWithGenreId.movie.title
        )

        Text(
            text = "TITULO: \n${movieWithGenreId.movie.title}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = contentInsetQuarter,
                    horizontal = contentInset
                ),
            fontSize = dynamicTwentyFourTextSize,
            color = Purple80,
            textAlign = TextAlign.Start,
            lineHeight = dynamicDisplayTextSize
        )
        Row(
            modifier = Modifier.padding(
                vertical = contentInsetQuarter,
                horizontal = contentInset
            )
        ) {
            Text(
                text = "${movieWithGenreId.movie.releaseDate}",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = contentInsetEight),
                fontSize = dynamicTwentyFourTextSize,
                color = Purple80,
                textAlign = TextAlign.Start,
                lineHeight = dynamicDisplayTextSize
            )
            Text(
                text = "${movieWithGenreId.movie.voteAverage}",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = contentInsetEight),
                fontSize = dynamicTwentyFourTextSize,
                color = Purple80,
                textAlign = TextAlign.Start,
                lineHeight = dynamicDisplayTextSize
            )
        }

        Text(
            text = "RESUMEN: \n${movieWithGenreId.movie.overview}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = contentInsetQuarter,
                    horizontal = contentInset
                ),
            fontSize = dynamicTwentyFourTextSize,
            color = Purple80,
            textAlign = TextAlign.Start,
            lineHeight = dynamicDisplayTextSize
        )
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