package com.mutkuensert.movee.ui.tvdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.movee.data.api.TvShowsApi
import com.mutkuensert.movee.data.model.remote.tvshows.TvDetailsModel
import com.mutkuensert.movee.data.model.remote.tvshows.credits.TvShowCast
import com.mutkuensert.movee.data.model.remote.tvshows.credits.TvShowCredits
import com.mutkuensert.movee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvDetailsViewModel @Inject constructor(private val tvShowsApi: TvShowsApi): ViewModel() {
    private val _tvDetails: MutableStateFlow<Resource<TvDetailsModel>> = MutableStateFlow(Resource.standby(null))
    val tvDetails: StateFlow<Resource<TvDetailsModel>> get() = _tvDetails

    private val _tvCast: MutableStateFlow<Resource<List<TvShowCast>>> = MutableStateFlow(Resource.standby(null))
    val tvCast: StateFlow<Resource<List<TvShowCast>>> get() = _tvCast

    fun getTvDetails(tvId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _tvDetails.value = Resource.loading(null)

            val response = tvShowsApi.getTvShowDetails(tvId = tvId)

            if(response.isSuccessful && response.body() != null){
                _tvDetails.value = Resource.success(response.body()!!)
            }else{
                _tvDetails.value = Resource.error("Unsuccessful Tv Details Request", null)
            }
        }
    }

    fun getTvCast(tvId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _tvCast.value = Resource.loading(null)

            val response = tvShowsApi.getTvShowCredits(tvId = tvId)

            if(response.isSuccessful && response.body() != null){
                _tvCast.value = Resource.success(response.body()!!.cast)
            }else{
                _tvCast.value = Resource.error("Unsuccessful Tv Credits Request", null)
            }
        }
    }
}