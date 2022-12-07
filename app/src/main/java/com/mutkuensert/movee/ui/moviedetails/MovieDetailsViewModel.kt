package com.mutkuensert.movee.ui.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.api.MovieApi
import com.mutkuensert.movee.data.model.remote.MovieDetailsModel
import com.mutkuensert.movee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val movieApi: MovieApi) : ViewModel() {
    private val _movieDetails: MutableStateFlow<Resource<MovieDetailsModel>> =
        MutableStateFlow(Resource.standby(null))
    val movieDetails: StateFlow<Resource<MovieDetailsModel>> get() = _movieDetails

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetails.value = Resource.loading(null)

            val response = movieApi.getMovieDetails(movieId)

            if (response.isSuccessful && response.body() != null) {
                _movieDetails.value = Resource.success(response.body()!!)
            } else {
                _movieDetails.value = Resource.error("Unsuccessful Popular Movies Request", null)
            }
        }
    }
}