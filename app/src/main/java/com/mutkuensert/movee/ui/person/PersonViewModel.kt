package com.mutkuensert.movee.ui.person

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.api.PersonApi
import com.mutkuensert.movee.data.model.remote.person.PersonDetailsModel
import com.mutkuensert.movee.data.model.remote.person.PersonMovieCastModel
import com.mutkuensert.movee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PersonViewModel"

@HiltViewModel
class PersonViewModel @Inject constructor(private val personApi: PersonApi): ViewModel() {
    private val _personDetails: MutableStateFlow<Resource<PersonDetailsModel>> = MutableStateFlow(
        Resource.standby(null))
    val personDetails: StateFlow<Resource<PersonDetailsModel>> get() = _personDetails

    private val _personCast: MutableStateFlow<Resource<List<PersonMovieCastModel>>> = MutableStateFlow(
        Resource.standby(null))
    val personCast: StateFlow<Resource<List<PersonMovieCastModel>>> get() = _personCast

    fun getPersonDetails(personId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _personDetails.value = Resource.loading(null)

            val response = personApi.getPersonDetails(personId = personId)

            if(response.isSuccessful && response.body() != null){
                _personDetails.value = Resource.success(response.body()!!)
            }else {
                _personDetails.value = Resource.error("Unsuccessful Person Details Request", null)
                Log.e(TAG, "Is response successful? ${response.isSuccessful}, Is response body null? ${response.body()}")
            }
        }
    }

    fun getPersonCast(personId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _personCast.value = Resource.loading(null)

            val response = personApi.getPersonMovieCredits(personId = personId)

            if(response.isSuccessful && response.body() != null){
                _personCast.value = Resource.success(response.body()!!.cast)
            }else {
                _personCast.value = Resource.error("Unsuccessful Person Cast Request", null)
                Log.e(TAG, "Is response successful? ${response.isSuccessful}, Is response body null? ${response.body()}")
            }
        }
    }
}