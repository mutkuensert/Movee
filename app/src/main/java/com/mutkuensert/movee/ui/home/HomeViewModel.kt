package com.mutkuensert.movee.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.PopularMovies
import com.mutkuensert.movee.data.source.RequestService
import com.mutkuensert.movee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(private val requestService: RequestService): ViewModel() {
    private val _popularMovies: MutableLiveData<Resource<PopularMovies?>> = MutableLiveData(Resource.standby(null))
    val popularMovies: LiveData<Resource<PopularMovies?>> get() = _popularMovies

    fun getPopularMovies(){
        viewModelScope.launch(Dispatchers.IO) {
            _popularMovies.postValue(Resource.loading(null))

            val response = requestService.getPopularMovies()
            if(response.isSuccessful && response.body() != null) {
                _popularMovies.postValue(Resource.success(response.body()!!))
            }else {
                _popularMovies.postValue(Resource.error("Either response is not successful or response body is null!", null))
                Log.e(TAG, "Either response is not successful or response body is null!")
            }
        }
    }
}