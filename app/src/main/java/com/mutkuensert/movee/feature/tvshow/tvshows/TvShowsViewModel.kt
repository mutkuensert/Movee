package com.mutkuensert.movee.feature.tvshow.tvshows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mutkuensert.movee.domain.tvshow.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) : ViewModel() {
    val popularTvShows = tvShowRepository.getPopularTvShowsPagingFlow()
        .cachedIn(viewModelScope)

    val topRatedTvShows = tvShowRepository.getTopRatedTvShowsPagingFlow()
        .cachedIn(viewModelScope)
}