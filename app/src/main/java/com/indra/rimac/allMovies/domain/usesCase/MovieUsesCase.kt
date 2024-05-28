package com.indra.rimac.allMovies.domain.usesCase

import androidx.paging.PagingData
import com.indra.rimac.allMovies.data.dataBase.pojo.MovieWithGenreId
import com.indra.rimac.allMovies.domain.models.Movie
import com.indra.rimac.allMovies.domain.models.ResponseAllMoviesDTO
import com.indra.rimac.allMovies.data.repository.cloud.RepositoryCloud
import com.indra.rimac.allMovies.data.repository.local.RepositoryLocal
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MovieUsesCase @Inject constructor(
    private val repositoryCloud: RepositoryCloud,
    private val repositoryLocal: RepositoryLocal
) {

    suspend operator fun invoke(
        page: Int, apiKey: String
    ): Flow<Response<ResponseAllMoviesDTO>> = repositoryCloud.getAllMovies(page, apiKey)

    suspend operator fun invoke(listId: List<Int>): Flow<PagingData<MovieWithGenreId>> =
        repositoryLocal.getListAllMovies(listId)

    suspend operator fun invoke(listMovies: List<Movie>, test: Boolean): Flow<Unit> =
        repositoryLocal.saveAllMovies(listMovies)

    suspend operator fun invoke(movieId: Int): Flow<MovieWithGenreId?> =
        repositoryLocal.getItemMovie(movieId)
}