package com.mutkuensert.movee.feature.tvshow.tvshows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.domain.tvshow.TvShowRepository
import com.mutkuensert.movee.navigation.navigator.TvShowNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val tvShowNavigator: TvShowNavigator,
    private val accountRepository: AccountRepository,
) : ViewModel() {
    val popularTvShows = tvShowRepository.getPopularTvShowsPagingFlow()
        .cachedIn(viewModelScope)

    val topRatedTvShows = tvShowRepository.getTopRatedTvShowsPagingFlow()
        .cachedIn(viewModelScope)

    fun navigateToTvShowDetails(tvShowId: Int) {
        tvShowNavigator.navigateToDetails(tvShowId)
    }

    fun addTvShowToFavorites(isFavorite: Boolean, movieId: Int) {
        viewModelScope.launch {
            accountRepository.addTvShowToFavorites(
                isFavorite = isFavorite,
                movieId = movieId
            )
        }
    }
}