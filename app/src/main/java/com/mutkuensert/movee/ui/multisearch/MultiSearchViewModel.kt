package com.mutkuensert.movee.ui.multisearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.api.MultiSearchApi
import com.mutkuensert.movee.data.datasource.paging.multisearch.MultiSearchResultsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MultiSearchViewModel @Inject constructor(private val multiSearchApi: MultiSearchApi): ViewModel() {

    var multiSearchResults = emptySearchResults()

    val searchTextField = MutableStateFlow<String>("")


    fun multiSearch(query: String){
        searchTextField.value = query
        if (query.length > 2){
            multiSearchResults = channelFlow {
                delay(1000)
                Pager(
                    PagingConfig(pageSize = 20)
                ) {
                    MultiSearchResultsPagingSource(
                        searchQuery = query,
                        multiSearchApi = multiSearchApi
                    )
                }.flow.cachedIn(viewModelScope).collectLatest {
                    send(it)
                }
            }
        }else{
            multiSearchResults = emptySearchResults()
        }
    }

    private fun emptySearchResults() = Pager(
        PagingConfig(pageSize = 20)
    ){
        MultiSearchResultsPagingSource(searchQuery = "", multiSearchApi = multiSearchApi)
    }.flow.cachedIn(viewModelScope)

}