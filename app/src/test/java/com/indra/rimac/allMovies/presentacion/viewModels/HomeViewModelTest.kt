package com.indra.rimac.allMovies.presentacion.viewModels

import com.indra.rimac.allMovies.base.BaseViewModelTest
import com.indra.rimac.allMovies.base.TestUtils
import com.indra.rimac.allMovies.base.TestUtils.responseNotSuccess
import com.indra.rimac.allMovies.base.TestUtils.responseSuccess
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.usesCase.MovieUsesCase
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventHome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class HomeViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    lateinit var movieUsesCase: MovieUsesCase
    lateinit var homeViewModel: HomeViewModel

    @Before
    @Throws(Exception::class)
    fun onBefore() {
        this.setUpBase()
        MockKAnnotations.init(this)
        homeViewModel = spyk(
            HomeViewModel(movieUsesCase)
        )
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `all movies when result is Success should handle correctly`() = runTest {
        val mockResult = TestUtils.responseResponseAllMoviesDTO.responseSuccess
        // Given
        coEvery { movieUsesCase.invoke(1, "") } returns flowOf(mockResult)
        // When
        homeViewModel.handleEvent(UiEventHome.LoadDataCloud)
        // Then
        coVerify(exactly = 1) { homeViewModel.handleEvent(UiEventHome.LoadDataCloud) }
        val result = homeViewModel.dataList.value
        assertEquals(mockResult.body()?.results, result)
    }
}