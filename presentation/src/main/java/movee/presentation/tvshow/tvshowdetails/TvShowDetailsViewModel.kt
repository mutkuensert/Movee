package movee.presentation.tvshow.tvshowdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import movee.domain.tvshow.TvShowRepository
import movee.domain.tvshow.model.Person
import movee.domain.tvshow.model.TvShowDetails
import movee.presentation.core.UiState
import movee.presentation.navigation.navigator.PersonNavigator
import movee.presentation.tvshow.navigation.KEY_TV_SHOW_ID

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val personNavigator: PersonNavigator,
    private val tvShowRepository: TvShowRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val tvShowId: Int = requireNotNull(savedStateHandle[KEY_TV_SHOW_ID]) {
        "Provide $KEY_TV_SHOW_ID before navigating."
    }
    private val _details: MutableStateFlow<UiState<TvShowDetails>> =
        MutableStateFlow(UiState.Empty())
    val details = _details.asStateFlow()

    private val _cast: MutableStateFlow<UiState<List<Person>>> =
        MutableStateFlow(UiState.Empty())
    val cast = _cast.asStateFlow()

    fun getDetails() {
        viewModelScope.launch {
            _details.value = UiState.Loading()

            tvShowRepository.getTvShowDetails(tvShowId = tvShowId).onSuccess {
                _details.value = UiState.Success(it)
            }
        }
    }

    fun getCast() {
        viewModelScope.launch {
            _cast.value = UiState.Loading()

            tvShowRepository.getTvShowCast(tvShowId = tvShowId).onSuccess {
                _cast.value = UiState.Success(it)
            }
        }
    }

    fun navigateToPersonDetails(personId: Int) {
        personNavigator.navigateToPerson(personId)
    }
}