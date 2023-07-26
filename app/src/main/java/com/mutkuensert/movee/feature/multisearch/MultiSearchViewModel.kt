package com.mutkuensert.movee.feature.multisearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mutkuensert.movee.data.search.MultiSearchApi
import com.mutkuensert.movee.data.search.MultiSearchResultsPagingSource
import com.mutkuensert.movee.data.search.model.MultiSearchResultMediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MultiSearchViewModel @Inject constructor(private val multiSearchApi: MultiSearchApi) :
    ViewModel() {
    private val loadStates = LoadStates(
        LoadState.NotLoading(true),
        LoadState.NotLoading(true),
        LoadState.NotLoading(true)
    )
    private val _multiSearchResults =
        MutableStateFlow<PagingData<MultiSearchResultMediaType>>(PagingData.empty(loadStates))
    val multiSearchResults = _multiSearchResults.asStateFlow()

    val searchTextField = MutableStateFlow<String>("")


    fun multiSearch(query: String) = viewModelScope.launch {
        searchTextField.value = query

        if (query.length > 2) {
            delay(1000)

            Pager(
                PagingConfig(pageSize = 20)
            ) {
                MultiSearchResultsPagingSource { page ->
                    multiSearchApi.multiSearch(query, page)
                }
            }.flow.cachedIn(viewModelScope).collectLatest {
                _multiSearchResults.value = it
            }
        } else {
            _multiSearchResults.value = PagingData.empty(loadStates)
        }
    }
}