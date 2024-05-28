package com.indra.rimac.allMovies.presentacion.viewModels

import com.indra.rimac.allMovies.base.BaseViewModelTest
import com.indra.rimac.allMovies.base.TestUtils
import com.indra.rimac.allMovies.data.state.UiState
import com.indra.rimac.allMovies.domain.usesCase.MovieUsesCase
import com.indra.rimac.allMovies.presentacion.interfaces.UiEventDetail
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class DetailViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    lateinit var movieUsesCase: MovieUsesCase
    lateinit var detailViewModel: DetailViewModel

    @Before
    @Throws(Exception::class)
    fun onBefore() {
        this.setUpBase()
        MockKAnnotations.init(this)
        detailViewModel = spyk(
            DetailViewModel(movieUsesCase)
        )
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `detail movies when result is Success should handle correctly`() = runTest {
        val mockResult = TestUtils.movieWithGenreId
        // Given
        coEvery { movieUsesCase.invoke(1) } returns flowOf(mockResult)
        // When
        detailViewModel.handleEvent(UiEventDetail.ObtainDetailMovie(1))
        // Then
        coVerify(exactly = 1) { detailViewModel.handleEvent(UiEventDetail.ObtainDetailMovie(1)) }
        val result = detailViewModel.movieWithGenreId.value
        Assert.assertEquals(mockResult, result)
    }

    @Test
    fun `detail movies when result not is Success should handle`() = runTest {
        val mockResult = null
        // Given
        coEvery { movieUsesCase.invoke(1) } returns flowOf(mockResult)
        // When
        detailViewModel.handleEvent(UiEventDetail.ObtainDetailMovie(1))
        // Then
        coVerify(exactly = 1) { detailViewModel.handleEvent(UiEventDetail.ObtainDetailMovie(1)) }
        val result = detailViewModel.getItemMovie.value
        print("Expected uiState to be NotSuccess with error but was $result")
        assert((result as UiState.NotSuccess).throwable.message == "Movie not found")
    }
}