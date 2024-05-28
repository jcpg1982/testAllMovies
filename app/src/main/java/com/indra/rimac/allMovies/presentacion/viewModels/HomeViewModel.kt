package com.indra.rimac.allMovies.presentacion.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.models.Movie
import com.indra.rimac.allMovies.domain.usesCase.MovieUsesCase
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventHome
import com.indra.rimac.allMovies.utils.helpers.Constants
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
class HomeViewModel @Inject constructor(private val movieUsesCase: MovieUsesCase) : ViewModel() {
    private val TAG = HomeViewModel::class.java.simpleName
    private var page = 1
    private var totalPages = 1

    private val _getAllMoviesCloud: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val getAllMoviesCloud = _getAllMoviesCloud.asStateFlow()

    private val _dataList: MutableStateFlow<List<Movie>> = MutableStateFlow(listOf())
    val dataList = _dataList.asStateFlow()

    init {
        handleEvent(UiEventHome.LoadDataCloud)
    }

    fun handleEvent(event: UiEventHome) {
        when (event) {
            UiEventHome.LoadDataCloud -> {
                page = 1
                getAllMoviesCloud()
            }

            UiEventHome.LoadMoreDataCloud -> {
                page++
                getAllMoviesCloud()
            }

            is UiEventHome.ViewData -> _dataList.update { dl -> dl + event.dataList }
            UiEventHome.LoadDataLocal -> getAllMoviesLocal()
            is UiEventHome.SaveDataLocal -> saveMoviesLocal(event.dataList)
        }
    }

    fun getAllMoviesCloud() {
        viewModelScope.launch {
            if (page <= totalPages) {
                movieUsesCase.invoke(page, Constants.APY_KEY)
                    .flowOn(Dispatchers.IO).onStart {
                        _getAllMoviesCloud.value = UiState.Loading(true, "Loading Movies")
                    }.catch { e ->
                        handleEvent(UiEventHome.LoadDataLocal)
                        _getAllMoviesCloud.value = UiState.NotSuccess(e)
                    }.collect {
                        totalPages = it.body()?.total_pages ?: 1
                        _getAllMoviesCloud.value = if (it.isSuccessful) {
                            it.body()?.results?.let { results ->
                                handleEvent(UiEventHome.ViewData(results))
                                handleEvent(UiEventHome.SaveDataLocal(results))
                            }
                            UiState.Success(it.body()?.results ?: listOf())
                        } else {
                            UiState.NotSuccess(Throwable(it.message()))
                        }
                    }
            }
        }
    }

    private fun getAllMoviesLocal() {
        viewModelScope.launch {
            movieUsesCase.invoke(dataList.value.map { it.id ?: 0 }).flowOn(Dispatchers.IO).onStart {
                _getAllMoviesCloud.value = UiState.Loading(true, "Loading Movies")
            }.catch { e ->
                _getAllMoviesCloud.value = UiState.NotSuccess(e)
            }.collect { result ->
                val listMovies = mutableListOf<Movie>()
                result.map { item ->
                    listMovies.add(Movie(item))
                }
                handleEvent(UiEventHome.ViewData(listMovies))
                _getAllMoviesCloud.value = UiState.Success(listMovies)
            }
        }
    }

    private fun saveMoviesLocal(listMovies: List<Movie>) {
        viewModelScope.launch {
            movieUsesCase.invoke(listMovies, false).flowOn(Dispatchers.IO).onStart {}.catch {}
                .collect {}
        }
    }
}