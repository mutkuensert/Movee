package com.mutkuensert.movee.feature.tvshow.tvshowdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.androidphase.Phase
import com.mutkuensert.movee.domain.tvshow.GetTvShowCastUseCase
import com.mutkuensert.movee.domain.tvshow.GetTvShowDetailsUseCase
import com.mutkuensert.movee.domain.tvshow.model.TvShowCast
import com.mutkuensert.movee.domain.tvshow.model.TvShowDetails
import com.mutkuensert.movee.feature.tvshow.navigation.KEY_TV_SHOW_ID
import com.mutkuensert.movee.navigation.navigator.PersonNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val personNavigator: PersonNavigator,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getTvShowCastUseCase: GetTvShowCastUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val tvShowId: Int = requireNotNull(savedStateHandle[KEY_TV_SHOW_ID]) {
        "Provide $KEY_TV_SHOW_ID before navigating."
    }
    private val _tvShowDetails: MutableStateFlow<Phase<TvShowDetails>> =
        MutableStateFlow(Phase.Standby())
    val tvShowDetails = _tvShowDetails.asStateFlow()

    private val _tvShowCast: MutableStateFlow<Phase<List<TvShowCast>>> =
        MutableStateFlow(Phase.Standby())
    val tvShowCast = _tvShowCast.asStateFlow()

    fun getTvShowDetails() {
        viewModelScope.launch {
            getTvShowDetailsUseCase.execute(tvShowId).collectLatest {
                _tvShowDetails.value = it
            }
        }
    }

    fun getTvShowCast() {
        viewModelScope.launch {
            getTvShowCastUseCase.execute(tvShowId).collectLatest {
                _tvShowCast.value = it
            }
        }
    }

    fun navigateToPersonDetails(personId: Int) {
        personNavigator.navigateToPerson(personId)
    }
}