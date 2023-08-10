package com.mutkuensert.movee.feature.movie.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.movie.MovieApi
import com.mutkuensert.movee.data.movie.source.MoviesNowPlayingPagingSource
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.domain.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val accountRepository: AccountRepository,
    private val movieRepository: MovieRepository,
) : ViewModel() {

    val popularMovies = movieRepository.getPopularMoviesFlow()
        .cachedIn(viewModelScope)

    val moviesNowPlaying = Pager(
        PagingConfig(pageSize = 20)
    ) {
        MoviesNowPlayingPagingSource(movieApi::getMoviesNowPlaying)
    }.flow.cachedIn(viewModelScope)

    fun addMovieToFavorites(isFavorite: Boolean, movieId: Int) {
        viewModelScope.launch {
            accountRepository.addMovieToFavorites(
                isFavorite = isFavorite,
                movieId = movieId
            )
        }
    }
}