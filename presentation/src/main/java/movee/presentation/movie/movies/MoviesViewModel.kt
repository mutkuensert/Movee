package movee.presentation.movie.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import movee.domain.movie.AddMovieToFavoriteUseCase
import movee.domain.movie.GetMoviesNowPlayingPagingFlowUseCase
import movee.domain.movie.GetPopularMoviesPagingFlowUseCase
import movee.presentation.navigation.navigator.MovieNavigator

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val addMovieToFavoriteUseCase: AddMovieToFavoriteUseCase,
    private val getMoviesNowPlayingPagingFlowUseCase: GetMoviesNowPlayingPagingFlowUseCase,
    private val getPopularMoviesPagingFlowUseCase: GetPopularMoviesPagingFlowUseCase,
    private val movieNavigator: MovieNavigator,
) : ViewModel() {

    val popularMovies = getPopularMoviesPagingFlowUseCase.execute()
        .cachedIn(viewModelScope)

    val moviesNowPlaying = getMoviesNowPlayingPagingFlowUseCase.execute()
        .cachedIn(viewModelScope)

    fun addMovieToFavorites(isFavorite: Boolean, movieId: Int) {
        viewModelScope.launch {
            addMovieToFavoriteUseCase.execute(
                isFavorite = isFavorite,
                movieId = movieId
            )
        }
    }

    fun navigateToMovieDetails(movieId: Int) {
        movieNavigator.navigateToDetails(movieId)
    }
}