package com.mutkuensert.movee.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.MovieDetailsModel
import com.mutkuensert.movee.data.source.RequestService
import com.mutkuensert.movee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val requestService: RequestService) : ViewModel() {
    private val _movieDetails: MutableLiveData<Resource<MovieDetailsModel>> = MutableLiveData(Resource.standby(null))
    val movieDetails: LiveData<Resource<MovieDetailsModel>> get() = _movieDetails

    fun getMovieDetails(movieId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetails.postValue(Resource.loading(null))

            val response = requestService.getMovieDetails(movieId)

            if(response.isSuccessful && response.body() != null){
                _movieDetails.postValue(Resource.success(response.body()!!))
            }else{
                _movieDetails.postValue(Resource.error("Unsuccessful Popular Movies Request", null))
            }
        }
    }
}