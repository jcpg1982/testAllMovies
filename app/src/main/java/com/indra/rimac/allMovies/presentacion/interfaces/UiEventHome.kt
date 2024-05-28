package com.indra.rimac.allMovies.presentacion.interfaces

import com.indra.rimac.allMovies.domain.models.Movie

sealed interface UiEventHome {
    data object LoadDataCloud : UiEventHome
    data object LoadMoreDataCloud : UiEventHome
    data object LoadDataLocal : UiEventHome
    data class SaveDataLocal(val dataList: List<Movie>) : UiEventHome
    data class ViewData(val dataList: List<Movie>) : UiEventHome
}
