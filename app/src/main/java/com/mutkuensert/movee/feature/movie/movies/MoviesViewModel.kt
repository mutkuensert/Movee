package com.mutkuensert.movee.feature.movie.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.movie.MovieApi
import com.mutkuensert.movee.data.movie.source.MoviesNowPlayingPagingSource
import com.mutkuensert.movee.domain.movie.GetPopularMoviesUseCase
import com.mutkuensert.movee.domain.movie.model.PopularMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {
    private val _popularMovies = MutableStateFlow(PagingData.empty<PopularMovie>())
    val popularMovies: Flow<PagingData<PopularMovie>>
        get() {
            viewModelScope.launch {
                getPopularMoviesUseCase.execute()
                    .cachedIn(viewModelScope)
                    .collectLatest {
                        _popularMovies.value = it
                    }
            }
            return _popularMovies.asStateFlow()
        }

    val moviesNowPlaying = Pager(
        PagingConfig(pageSize = 20)
    ) {
        MoviesNowPlayingPagingSource(movieApi::getMoviesNowPlaying)
    }.flow.cachedIn(viewModelScope)
}