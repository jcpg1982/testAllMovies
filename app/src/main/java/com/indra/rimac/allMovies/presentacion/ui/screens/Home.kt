package com.indra.rimac.allMovies.presentacion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.models.Routes
import com.indra.rimac.allMovies.domain.models.Routes.DetailMovie.Companion.createDetail
import com.indra.rimac.allMovies.presentacion.dialog.DialogAlert
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventHome
import com.indra.rimac.allMovies.presentacion.theme.ColorWhite
import com.indra.rimac.allMovies.presentacion.theme.Purple80
import com.indra.rimac.allMovies.presentacion.theme.contentInsetFifty
import com.indra.rimac.allMovies.presentacion.theme.contentInsetTen
import com.indra.rimac.allMovies.presentacion.theme.contentInsetTwo
import com.indra.rimac.allMovies.presentacion.ui.composables.RowMovieComposable
import com.indra.rimac.allMovies.presentacion.viewModels.HomeViewModel
import com.indra.rimac.allMovies.presentacion.viewModels.NavigationViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Home(navigationViewModel: NavigationViewModel) {

    val homeViewModel: HomeViewModel = hiltViewModel()
    val getAllMoviesCloud by homeViewModel.getAllMoviesCloud.collectAsState()
    val dataList by homeViewModel.dataList.collectAsState()

    var messageLoading by rememberSaveable { mutableStateOf(value = "") }
    var messageError by rememberSaveable { mutableStateOf(value = "") }

    val listState = rememberLazyListState()

    LaunchedEffect(getAllMoviesCloud) {
        when (val state = getAllMoviesCloud) {
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
            .fillMaxWidth()
            .background(color = ColorWhite)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(contentInsetTen)
        ) {
            items(dataList) { item ->
                RowMovieComposable(movie = item, onDetailClick = {
                    item.id?.let { id -> navigationViewModel.onNavigateTo(id.createDetail(item.title.orEmpty())) }
                }, onImageClick = {})
            }
            if (messageLoading.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(contentInsetTen),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(contentInsetFifty),
                            strokeWidth = contentInsetTwo
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collectLatest { index ->
            if (messageLoading.isEmpty() && index != null && index >= dataList.size - 1) {
                homeViewModel.handleEvent(UiEventHome.LoadMoreDataCloud)
            }
        }
    }

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