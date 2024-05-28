package com.indra.rimac.allMovies.presentacion.interfaces

import com.indra.rimac.allMovies.data.dataBase.pojo.MovieWithGenreId

sealed interface UiEventDetail {
    data class ObtainDetailMovie(val movieId: Int) : UiEventDetail
    data class ViewDetailMovie(val data: MovieWithGenreId) : UiEventDetail
}
