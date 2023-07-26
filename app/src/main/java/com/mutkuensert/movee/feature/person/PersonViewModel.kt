package com.mutkuensert.movee.feature.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.person.PersonApi
import com.mutkuensert.movee.data.person.model.PersonDetailsModel
import com.mutkuensert.movee.data.person.model.PersonMovieCastModel
import com.mutkuensert.movee.data.person.model.PersonTvCastModel
import com.mutkuensert.movee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PersonViewModel @Inject constructor(private val personApi: PersonApi) : ViewModel() {
    private val _personDetails: MutableStateFlow<Resource<PersonDetailsModel>> =
        MutableStateFlow(Resource.standby(null))
    val personDetails = _personDetails.asStateFlow()

    private val _personMovieCast: MutableStateFlow<Resource<List<PersonMovieCastModel>>> =
        MutableStateFlow(Resource.standby(null))
    val personMovieCast = _personMovieCast.asStateFlow()

    private val _personTvCast: MutableStateFlow<Resource<List<PersonTvCastModel>>> =
        MutableStateFlow(Resource.standby(null))
    val personTvCast = _personTvCast.asStateFlow()

    fun getPersonDetails(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _personDetails.value = Resource.loading(null)

            val response = personApi.getPersonDetails(personId = personId)

            if (response.isSuccessful && response.body() != null) {
                _personDetails.value = Resource.success(response.body()!!)
            } else {
                _personDetails.value = Resource.error("Unsuccessful Person Details Request", null)
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
        _personMovieCast.value = Resource.loading(null)

        val response = personApi.getPersonMovieCredits(personId = personId)

        if (response.isSuccessful && response.body() != null) {
            _personMovieCast.value = Resource.success(response.body()!!.cast)
        } else {
            _personMovieCast.value = Resource.error("Unsuccessful Person Movie Cast Request", null)
        }
    }

    private suspend fun getPersonTvCast(personId: Int) {
        _personTvCast.value = Resource.loading(null)

        val response = personApi.getPersonTvCredits(personId = personId)

        if (response.isSuccessful && response.body() != null) {
            _personTvCast.value = Resource.success(response.body()!!.cast)
        } else {
            _personTvCast.value = Resource.error("Unsuccessful Person Tv Cast Request", null)
        }
    }
}