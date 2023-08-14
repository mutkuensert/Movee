package com.mutkuensert.movee.feature.movie.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.domain.movie.MovieRepository
import com.mutkuensert.movee.navigation.navigator.MovieNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val movieRepository: MovieRepository,
    private val movieNavigator: MovieNavigator,
) : ViewModel() {

    val popularMovies = movieRepository.getPopularMoviesPagingFlow()
        .cachedIn(viewModelScope)

    val moviesNowPlaying = movieRepository.getMoviesNowPlayingPagingFlow()
        .cachedIn(viewModelScope)

    fun addMovieToFavorites(isFavorite: Boolean, movieId: Int) {
        viewModelScope.launch {
            accountRepository.addMovieToFavorites(
                isFavorite = isFavorite,
                movieId = movieId
            )
        }
    }

    fun navigateToMovieDetails(movieId: Int) {
        movieNavigator.navigateToDetails(movieId)
    }
}