package com.indra.rimac.allMovies.data.repository.cloud

import com.indra.rimac.allMovies.domain.models.ResponseAllMoviesDTO
import com.indra.rimac.allMovies.data.repository.WebService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.indra.rimac.allMovies.data.repository.dataSource.DataSource
import retrofit2.Response
import javax.inject.Inject

class RepositoryCloud @Inject constructor(private val webService: WebService) :
    DataSource.DataSourceCloud {

    override suspend fun getAllMovies(
        page: Int, apiKey: String
    ): Flow<Response<ResponseAllMoviesDTO>> = flow {
        val result = webService.getAllMovies(page, apiKey)
        emit(result)
    }
}