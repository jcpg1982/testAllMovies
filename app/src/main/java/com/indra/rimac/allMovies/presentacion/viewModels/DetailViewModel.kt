package com.indra.rimac.allMovies.presentacion.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indra.rimac.allMovies.data.dataBase.pojo.MovieWithGenreId
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.usesCase.MovieUsesCase
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieUsesCase: MovieUsesCase) : ViewModel() {
    private val TAG = DetailViewModel::class.java.simpleName

    private val _getItemMovie: MutableStateFlow<UiState<MovieWithGenreId>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val getItemMovie = _getItemMovie.asStateFlow()


    private val _movieWithGenreId: MutableStateFlow<MovieWithGenreId> =
        MutableStateFlow(MovieWithGenreId())
    val movieWithGenreId = _movieWithGenreId.asStateFlow()

    fun handleEvent(event: UiEventDetail) {
        when (event) {
            is UiEventDetail.ObtainDetailMovie -> getItemMovie(event.movieId)
            is UiEventDetail.ViewDetailMovie -> _movieWithGenreId.update { event.data }
        }
    }

    private fun getItemMovie(movieId: Int) {
        viewModelScope.launch {
            movieUsesCase.invoke(movieId).flowOn(Dispatchers.IO).onStart {
                _getItemMovie.value = UiState.Loading(true, "Loading Movies")
            }.catch { e ->
                _getItemMovie.value = UiState.NotSuccess(e)
            }.collect {
                _getItemMovie.value = if (it != null) {
                    handleEvent(UiEventDetail.ViewDetailMovie(it))
                    UiState.Success(it)
                } else {
                    UiState.NotSuccess(Throwable("Movie not found"))
                }
            }
        }
    }
}