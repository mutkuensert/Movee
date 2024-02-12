package movee.presentation.movie.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import movee.domain.movie.MovieRepository
import movee.domain.movie.model.MovieDetails
import movee.domain.movie.model.Person
import movee.presentation.core.UiState
import movee.presentation.movie.navigation.KEY_MOVIE_ID
import movee.presentation.navigation.navigator.PersonNavigator

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val personNavigator: PersonNavigator,
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId: Int = requireNotNull(savedStateHandle[KEY_MOVIE_ID]) {
        "Provide $KEY_MOVIE_ID before navigating."
    }
    private val _details: MutableStateFlow<UiState<MovieDetails>> =
        MutableStateFlow(UiState.Empty())
    val details = _details.asStateFlow()

    private val _cast: MutableStateFlow<UiState<List<Person>>> =
        MutableStateFlow(UiState.Empty())
    val cast = _cast.asStateFlow()

    fun getDetails() {
        viewModelScope.launch {
            _details.value = UiState.Loading()

            movieRepository.getMovieDetails(movieId = movieId).onSuccess {
                _details.value = UiState.Success(it)
            }
        }
    }

    fun getCast() {
        viewModelScope.launch {
            _cast.value = UiState.Loading()

            movieRepository.getMovieCast(movieId = movieId).onSuccess {
                _cast.value = UiState.Success(it)
            }
        }
    }

    fun navigateToPerson(personId: Int) {
        personNavigator.navigateToPerson(personId)
    }
}