package movee.presentation.tvshow.tvshowdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.androidphase.Phase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import movee.domain.tvshow.GetTvShowCastUseCase
import movee.domain.tvshow.GetTvShowDetailsUseCase
import movee.domain.tvshow.model.Person
import movee.domain.tvshow.model.TvShowDetails
import movee.presentation.navigation.navigator.PersonNavigator
import movee.presentation.tvshow.navigation.KEY_TV_SHOW_ID

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val personNavigator: PersonNavigator,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getTvShowCastUseCase: GetTvShowCastUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val tvShowId: Int = requireNotNull(savedStateHandle[KEY_TV_SHOW_ID]) {
        "Provide $KEY_TV_SHOW_ID before navigating."
    }
    private val _details: MutableStateFlow<Phase<TvShowDetails>> =
        MutableStateFlow(Phase.Standby())
    val details = _details.asStateFlow()

    private val _cast: MutableStateFlow<Phase<List<Person>>> =
        MutableStateFlow(Phase.Standby())
    val cast = _cast.asStateFlow()

    fun getDetails() {
        viewModelScope.launch {
            getTvShowDetailsUseCase.execute(tvShowId).collectLatest {
                _details.value = it
            }
        }
    }

    fun getCast() {
        viewModelScope.launch {
            getTvShowCastUseCase.execute(tvShowId).collectLatest {
                _cast.value = it
            }
        }
    }

    fun navigateToPersonDetails(personId: Int) {
        personNavigator.navigateToPerson(personId)
    }
}