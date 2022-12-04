package com.mutkuensert.movee.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.source.PopularMoviesPagingSource
import com.mutkuensert.movee.data.source.RequestService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(private val requestService: RequestService): ViewModel() {

    val flow = Pager(
        PagingConfig(pageSize = 20)
    ){
        PopularMoviesPagingSource(requestService)
    }.flow.cachedIn(viewModelScope)

}