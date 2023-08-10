package com.mutkuensert.movee.feature.movie.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.movie.MovieApi
import com.mutkuensert.movee.data.movie.source.MoviesNowPlayingPagingSource
import com.mutkuensert.movee.domain.movie.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {

    val popularMovies = getPopularMoviesUseCase.execute()
        .cachedIn(viewModelScope)

    val moviesNowPlaying = Pager(
        PagingConfig(pageSize = 20)
    ) {
        MoviesNowPlayingPagingSource(movieApi::getMoviesNowPlaying)
    }.flow.cachedIn(viewModelScope)
}