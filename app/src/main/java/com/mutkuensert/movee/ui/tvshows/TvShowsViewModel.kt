package com.mutkuensert.movee.ui.tvshows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.api.TvShowsApi
import com.mutkuensert.movee.data.datasource.paging.tvshows.PopularTvShowsPagingSource
import com.mutkuensert.movee.data.datasource.paging.tvshows.TopRatedTvShowsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val tvShowsApi: TvShowsApi) : ViewModel() {
    val popularTvShows = Pager(
        PagingConfig(pageSize = 20)
    ) {
        PopularTvShowsPagingSource(tvShowsApi)
    }.flow.cachedIn(viewModelScope)

    val topRatedTvShows = Pager(
        PagingConfig(pageSize = 20)
    ) {
        TopRatedTvShowsPagingSource(tvShowsApi)
    }.flow.cachedIn(viewModelScope)


}