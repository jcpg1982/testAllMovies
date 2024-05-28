package com.indra.rimac.allMovies.domain.models

data class ResponseAllMoviesDTO(
    val dates: Dates? = null,
    val page: Int? = null,
    val results: List<Movie>? = null,
    val total_pages: Int? = null,
    val total_results: Int? = null
)