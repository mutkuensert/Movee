package movee.presentation.multisearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import movee.domain.multisearch.GetMultiSearchPagingFlowUseCase
import movee.domain.multisearch.model.SearchResult
import movee.presentation.navigation.navigator.MovieNavigator
import movee.presentation.navigation.navigator.PersonNavigator
import movee.presentation.navigation.navigator.TvShowNavigator

@HiltViewModel
class MultiSearchViewModel @Inject constructor(
    private val getMultiSearchPagingFlowUseCase: GetMultiSearchPagingFlowUseCase,
    private val movieNavigator: MovieNavigator,
    private val personNavigator: PersonNavigator,
    private val tvShowNavigator: TvShowNavigator,
) : ViewModel() {
    private var _multiSearchResults = MutableStateFlow<PagingData<SearchResult>>(
        PagingData.empty(
            LoadStates(
                refresh = LoadState.NotLoading(true),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
    )
    val multiSearchResults = _multiSearchResults.asStateFlow()

    private val _searchTextField = MutableStateFlow("")
    val searchTextField = _searchTextField.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _searchTextField.value = query

            getMultiSearchPagingFlowUseCase.execute(query)
                .cachedIn(viewModelScope)
                .collect {
                    _multiSearchResults.value = it
                }
        }
    }

    fun navigateToPersonDetails(personId: Int) {
        personNavigator.navigateToPerson(personId)
    }

    fun navigateToTvShowDetails(tvShowId: Int) {
        tvShowNavigator.navigateToDetails(tvShowId)
    }

    fun navigateToMovieDetails(movieId: Int) {
        movieNavigator.navigateToDetails(movieId)
    }
}