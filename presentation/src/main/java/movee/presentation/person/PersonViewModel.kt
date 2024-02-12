package movee.presentation.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import movee.domain.person.PersonRepository
import movee.domain.person.model.PersonDetails
import movee.domain.person.model.PersonMovieCasting
import movee.domain.person.model.PersonTvCasting
import movee.presentation.core.UiState
import movee.presentation.navigation.navigator.MovieNavigator
import movee.presentation.navigation.navigator.TvShowNavigator
import movee.presentation.person.navigation.KEY_PERSON_ID

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val movieNavigator: MovieNavigator,
    private val tvShowNavigator: TvShowNavigator,
    private val personRepository: PersonRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val personId: Int = requireNotNull(savedStateHandle[KEY_PERSON_ID]) {
        "Provide $KEY_PERSON_ID before navigating."
    }
    private val _details: MutableStateFlow<UiState<PersonDetails>> =
        MutableStateFlow(UiState.Empty())
    val details = _details.asStateFlow()

    private val _movieCasting: MutableStateFlow<UiState<List<PersonMovieCasting>>> =
        MutableStateFlow(UiState.Empty())
    val movieCasting = _movieCasting.asStateFlow()

    private val _tvCasting: MutableStateFlow<UiState<List<PersonTvCasting>>> =
        MutableStateFlow(UiState.Empty())
    val tvCasting = _tvCasting.asStateFlow()

    fun getPersonDetails() {
        viewModelScope.launch {
            _details.value = UiState.Loading()

            personRepository.getPersonDetails(personId = personId).onSuccess {
                _details.value = UiState.Success(it)
            }
        }
    }

    fun getCasting() {
        viewModelScope.launch {
            getMovieCasting(personId = personId)
            getTvCasting(personId = personId)
        }
    }

    private suspend fun getMovieCasting(personId: Int) {
        viewModelScope.launch {
            _movieCasting.value = UiState.Loading()

            personRepository.getPersonMovieCasting(personId = personId).onSuccess {
                _movieCasting.value = UiState.Success(it)
            }
        }
    }

    private suspend fun getTvCasting(personId: Int) {
        viewModelScope.launch {
            _tvCasting.value = UiState.Loading()

            personRepository.getPersonTvCasting(personId = personId).onSuccess {
                _tvCasting.value = UiState.Success(it)
            }
        }
    }

    fun navigateToMovieDetails(movieId: Int) {
        movieNavigator.navigateToDetails(movieId)
    }

    fun navigateToTvShowDetails(tvShowId: Int) {
        tvShowNavigator.navigateToDetails(tvShowId)
    }
}