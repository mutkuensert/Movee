package com.mutkuensert.movee.feature.movie.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.movie.MovieApi
import com.mutkuensert.movee.data.movie.remote.model.MovieCastDto
import com.mutkuensert.movee.data.movie.remote.model.MovieDetailsResponse
import com.mutkuensert.movee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val movieApi: MovieApi) : ViewModel() {
    private val _movieDetails: MutableStateFlow<Resource<MovieDetailsResponse>> =
        MutableStateFlow(Resource.standby(null))
    val movieDetails = _movieDetails.asStateFlow()

    private val _movieCastDto: MutableStateFlow<Resource<List<MovieCastDto>>> =
        MutableStateFlow(Resource.standby(null))
    val movieCast = _movieCastDto.asStateFlow()

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
            _movieCastDto.value = Resource.loading(null)

            val response = movieApi.getMovieCredits(movieId = movieId)

            if (response.isSuccessful && response.body() != null) {
                _movieCastDto.value = Resource.success(response.body()!!.cast)
            } else {
                _movieCastDto.value =
                    Resource.error("Unsuccessful Movie Credits Request", null)
            }
        }
    }
}