package com.mutkuensert.movee.feature.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.domain.GetResourceFlowUseCase
import com.mutkuensert.movee.domain.person.PersonRepository
import com.mutkuensert.movee.domain.person.model.PersonDetails
import com.mutkuensert.movee.domain.person.model.PersonMovieCast
import com.mutkuensert.movee.domain.person.model.PersonTvCast
import com.mutkuensert.movee.domain.util.Resource
import com.mutkuensert.movee.feature.person.navigation.KEY_PERSON_ID
import com.mutkuensert.movee.navigation.navigator.MovieNavigator
import com.mutkuensert.movee.navigation.navigator.TvShowNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val movieNavigator: MovieNavigator,
    private val tvShowNavigator: TvShowNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val personId: Int = requireNotNull(savedStateHandle[KEY_PERSON_ID]) {
        "Provide $KEY_PERSON_ID before navigating."
    }
    private val _details: MutableStateFlow<Resource<PersonDetails>> =
        MutableStateFlow(Resource.Standby())
    val details = _details.asStateFlow()

    private val _movieCastingResource: MutableStateFlow<Resource<List<PersonMovieCast>>> =
        MutableStateFlow(Resource.Standby())
    val movieCastingResource = _movieCastingResource.asStateFlow()

    private val _tvCastingResource: MutableStateFlow<Resource<List<PersonTvCast>>> =
        MutableStateFlow(Resource.Standby(null))
    val tvCastingResource = _tvCastingResource.asStateFlow()

    fun getPersonDetails(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            GetResourceFlowUseCase<PersonDetails>().execute {
                personRepository.getPersonDetails(personId)
            }.collect {
                _details.value = it
            }
        }
    }

    fun getCasting(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getMovieCasting(personId = personId)
            getTvCasting(personId = personId)
        }
    }

    private suspend fun getMovieCasting(personId: Int) {
        viewModelScope.launch {
            GetResourceFlowUseCase<List<PersonMovieCast>>().execute {
                personRepository.getPersonMovieCasting(personId)
            }.collect {
                _movieCastingResource.value = it
            }
        }
    }

    private suspend fun getTvCasting(personId: Int) {
        viewModelScope.launch {
            GetResourceFlowUseCase<List<PersonTvCast>>().execute {
                personRepository.getPersonTvCasting(personId)
            }.collect {
                _tvCastingResource.value = it
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