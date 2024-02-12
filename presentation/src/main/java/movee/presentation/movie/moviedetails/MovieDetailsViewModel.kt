package movee.presentation.movie.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.androidphase.Phase
import movee.presentation.movie.navigation.KEY_MOVIE_ID
import movee.presentation.navigation.navigator.PersonNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import movee.domain.movie.GetMovieCastUseCase
import movee.domain.movie.GetMovieDetailsUseCase
import movee.domain.movie.model.Person
import movee.domain.movie.model.MovieDetails

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val personNavigator: PersonNavigator,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId: Int = requireNotNull(savedStateHandle[KEY_MOVIE_ID]) {
        "Provide $KEY_MOVIE_ID before navigating."
    }
    private val _details: MutableStateFlow<Phase<MovieDetails>> =
        MutableStateFlow(Phase.Standby())
    val details = _details.asStateFlow()

    private val _person: MutableStateFlow<Phase<List<Person>>> =
        MutableStateFlow(Phase.Standby())
    val person = _person.asStateFlow()

    fun getDetails() {
        viewModelScope.launch {
            getMovieDetailsUseCase.execute(movieId).collectLatest {
                _details.value = it
            }
        }
    }

    fun getCast() {
        viewModelScope.launch {
            getMovieCastUseCase.execute(movieId).collectLatest {
                _person.value = it
            }
        }
    }

    fun navigateToPerson(personId: Int) {
        personNavigator.navigateToPerson(personId)
    }
}