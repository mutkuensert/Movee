package com.mutkuensert.movee.feature.movie.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.movie.MovieApi
import com.mutkuensert.movee.data.movie.model.MovieDetailsModel
import com.mutkuensert.movee.data.movie.model.credits.MovieCast
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

    private val _movieCast: MutableStateFlow<Resource<List<MovieCast>>> =
        MutableStateFlow(Resource.standby(null))
    val movieCast: StateFlow<Resource<List<MovieCast>>> get() = _movieCast

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetails.value = Resource.loading(null)

            val response = movieApi.getMovieDetails(movieId)

            if (response.isSuccessful && response.body() != null) {
                _movieDetails.value = Resource.success(response.body()!!)
            } else {
                _movieDetails.value = Resource.error("Unsuccessful Movie Details Request", null)
            }
        }
    }

    fun getMovieCast(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _movieCast.value = Resource.loading(null)

            val response = movieApi.getMovieCredits(movieId = movieId)

            if (response.isSuccessful && response.body() != null) {
                _movieCast.value = Resource.success(response.body()!!.cast)
            } else {
                _movieCast.value = Resource.error("Unsuccessful Movie Credits Request", null)
            }
        }
    }
}