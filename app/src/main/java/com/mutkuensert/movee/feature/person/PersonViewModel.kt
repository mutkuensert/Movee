package com.mutkuensert.movee.feature.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.domain.GetResourceFlowUseCase
import com.mutkuensert.movee.domain.person.PersonRepository
import com.mutkuensert.movee.domain.person.model.PersonDetails
import com.mutkuensert.movee.domain.person.model.PersonMovieCast
import com.mutkuensert.movee.domain.person.model.PersonTvCast
import com.mutkuensert.movee.feature.person.navigation.KEY_PERSON_ID
import com.mutkuensert.movee.navigation.navigator.MovieNavigator
import com.mutkuensert.movee.navigation.navigator.TvShowNavigator
import com.mutkuensert.movee.util.Resource
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
    private val _personDetails = MutableStateFlow(Resource.standby<PersonDetails>(null))
    val personDetails = _personDetails.asStateFlow()

    private val _personMovieCast =
        MutableStateFlow(Resource.standby<List<PersonMovieCast>>(null))
    val personMovieCast = _personMovieCast.asStateFlow()

    private val _personTvCast = MutableStateFlow(Resource.standby<List<PersonTvCast>>(null))
    val personTvCast = _personTvCast.asStateFlow()

    fun getPersonDetails(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            GetResourceFlowUseCase<PersonDetails>().execute {
                personRepository.getPersonDetails(personId)
            }.collect {
                _personDetails.value = it
            }
        }
    }

    fun getPersonCast(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getPersonMovieCast(personId = personId)
            getPersonTvCast(personId = personId)
        }
    }

    private suspend fun getPersonMovieCast(personId: Int) {
        viewModelScope.launch {
            GetResourceFlowUseCase<List<PersonMovieCast>>().execute {
                personRepository.getPersonMovieCast(personId)
            }.collect {
                _personMovieCast.value = it
            }
        }
    }

    private suspend fun getPersonTvCast(personId: Int) {
        viewModelScope.launch {
            GetResourceFlowUseCase<List<PersonTvCast>>().execute {
                personRepository.getPersonTvCast(personId)
            }.collect {
                _personTvCast.value = it
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