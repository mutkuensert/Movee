package movee.presentation.tvshow.tvshows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import movee.domain.tvshow.TvShowRepository
import movee.presentation.navigation.navigator.TvShowNavigator

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val tvShowNavigator: TvShowNavigator,
    private val accountRepository: movee.domain.account.AccountRepository,
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