package com.mutkuensert.movee.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.api.MovieApi
import com.mutkuensert.movee.data.datasource.paging.MoviesNowPlayingPagingSource
import com.mutkuensert.movee.data.datasource.paging.PopularMoviesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "MoviesViewModel"

@HiltViewModel
class MoviesViewModel @Inject constructor(private val movieApi: MovieApi) : ViewModel() {

    val popularMovies = Pager(
        PagingConfig(pageSize = 20)
    ) {
        PopularMoviesPagingSource(movieApi)
    }.flow.cachedIn(viewModelScope)


    val moviesNowPlaying = Pager(
        PagingConfig(pageSize = 20)
    ) {
        MoviesNowPlayingPagingSource(movieApi)
    }.flow.cachedIn(viewModelScope)

}