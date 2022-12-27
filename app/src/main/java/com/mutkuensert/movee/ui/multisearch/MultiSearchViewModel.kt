package com.mutkuensert.movee.ui.multisearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mutkuensert.movee.data.api.MultiSearchApi
import com.mutkuensert.movee.data.datasource.paging.multisearch.MultiSearchResultsPagingSource
import com.mutkuensert.movee.data.model.remote.search.MultiSearchResultMediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiSearchViewModel @Inject constructor(private val multiSearchApi: MultiSearchApi) :
    ViewModel() {

    private val loadStates = LoadStates(
        LoadState.NotLoading(true),
        LoadState.NotLoading(true),
        LoadState.NotLoading(true)
    )

    var multiSearchResults = MutableStateFlow<PagingData<MultiSearchResultMediaType>>(PagingData.empty(loadStates))

    val searchTextField = MutableStateFlow<String>("")


    fun multiSearch(query: String) = viewModelScope.launch {
        searchTextField.value = query
        if (query.length > 2) {
            delay(1000)
            Pager(
                PagingConfig(pageSize = 20)
            ) {
                MultiSearchResultsPagingSource(
                    searchQuery = query,
                    multiSearchApi = multiSearchApi
                )
            }.flow.cachedIn(viewModelScope).collectLatest {
                multiSearchResults.value = it
            }
        } else {
            multiSearchResults.value = PagingData.empty(loadStates)
        }
    }
}