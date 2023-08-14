package com.mutkuensert.movee.feature.tvshow.tvshowdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.domain.GetResourceFlowUseCase
import com.mutkuensert.movee.domain.tvshow.TvShowRepository
import com.mutkuensert.movee.domain.tvshow.model.TvShowCast
import com.mutkuensert.movee.domain.tvshow.model.TvShowDetails
import com.mutkuensert.movee.feature.tvshow.navigation.KEY_TV_SHOW_ID
import com.mutkuensert.movee.navigation.navigator.PersonNavigator
import com.mutkuensert.movee.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val personNavigator: PersonNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val tvShowId: Int = requireNotNull(savedStateHandle[KEY_TV_SHOW_ID]) {
        "Provide $KEY_TV_SHOW_ID before navigating."
    }
    private val _tvShowDetails: MutableStateFlow<Resource<TvShowDetails>> =
        MutableStateFlow(Resource.standby(null))
    val tvShowDetails = _tvShowDetails.asStateFlow()

    private val _tvCast: MutableStateFlow<Resource<List<TvShowCast>>> =
        MutableStateFlow(Resource.standby(null))
    val tvCast = _tvCast.asStateFlow()

    fun getTvDetails(tvId: Int) {
        viewModelScope.launch {
            GetResourceFlowUseCase<TvShowDetails>().execute {
                tvShowRepository.getTvShowDetails(tvId)
            }.collect {
                _tvShowDetails.value = it
            }
        }
    }

    fun getTvShowCast(tvShowId: Int) {
        viewModelScope.launch {
            GetResourceFlowUseCase<List<TvShowCast>>().execute {
                tvShowRepository.getTvShowCast(tvShowId)
            }.collect {
                _tvCast.value = it
            }
        }
    }

    fun navigateToPersonDetails(personId: Int) {
        personNavigator.navigateToPerson(personId)
    }
}