package com.indra.rimac.allMovies.data.repository

import com.indra.rimac.allMovies.BuildConfig
import com.indra.rimac.allMovies.domain.models.ResponseAllMoviesDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET(BuildConfig.ALL_MOVIES)
    suspend fun getAllMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Response<ResponseAllMoviesDTO>
}