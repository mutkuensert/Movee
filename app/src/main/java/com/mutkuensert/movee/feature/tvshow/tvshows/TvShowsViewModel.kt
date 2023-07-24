package com.mutkuensert.movee.feature.tvshow.tvshows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.tvshow.TvShowsApi
import com.mutkuensert.movee.data.tvshow.source.PopularTvShowsPagingSource
import com.mutkuensert.movee.data.tvshow.source.TopRatedTvShowsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val tvShowsApi: TvShowsApi) : ViewModel() {
    val popularTvShows = Pager(
        PagingConfig(pageSize = 20)
    ) {
        PopularTvShowsPagingSource(tvShowsApi::getPopularTvShows)
    }.flow.cachedIn(viewModelScope)

    val topRatedTvShows = Pager(
        PagingConfig(pageSize = 20)
    ) {
        TopRatedTvShowsPagingSource(tvShowsApi::getTopRatedTvShows)
    }.flow.cachedIn(viewModelScope)


}