package com.mutkuensert.movee.ui.popularmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.source.MoviesNowPlayingPagingSource
import com.mutkuensert.movee.data.source.PopularMoviesPagingSource
import com.mutkuensert.movee.data.source.RequestService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "PopularMoviesViewModel"

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(private val requestService: RequestService): ViewModel() {

    val popularMovies = Pager(
        PagingConfig(pageSize = 20)
    ){
        PopularMoviesPagingSource(requestService)
    }.flow.cachedIn(viewModelScope)


    val moviesNowPlaying = Pager(
        PagingConfig(pageSize = 20)
    ){
        MoviesNowPlayingPagingSource(requestService)
    }.flow.cachedIn(viewModelScope)

}