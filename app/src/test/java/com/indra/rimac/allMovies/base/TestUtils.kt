package com.indra.rimac.allMovies.base

import com.indra.rimac.allMovies.data.dataBase.pojo.MovieWithGenreId
import com.indra.rimac.allMovies.domain.models.ResponseAllMoviesDTO
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

object TestUtils {

    val responseResponseAllMoviesDTO
        get() = ResponseAllMoviesDTO(
            results = listOf()
        )

    val movieWithGenreId
        get() = MovieWithGenreId()

    val <T> T.responseSuccess: Response<T> get() = Response.success(this)
    fun <T> T.responseNotSuccess(code: Int, message: String): Response<T> =
        Response.error(code, message.toResponseBody(null))
}