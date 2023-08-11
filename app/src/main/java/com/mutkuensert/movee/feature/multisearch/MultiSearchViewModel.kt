package com.mutkuensert.movee.feature.multisearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mutkuensert.movee.domain.multisearch.MultiSearchRepository
import com.mutkuensert.movee.domain.multisearch.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MultiSearchViewModel @Inject constructor(
    private val multiSearchRepository: MultiSearchRepository
) : ViewModel() {
    private var _multiSearchResults = MutableStateFlow<PagingData<SearchResult>>(
        PagingData.empty(
            LoadStates(
                refresh = LoadState.NotLoading(true),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
    )
    val multiSearchResults = _multiSearchResults.asStateFlow()

    val searchTextField = MutableStateFlow("")


    fun multiSearch(query: String) {
        viewModelScope.launch {
            searchTextField.value = query

            multiSearchRepository.getSearchFlow(query)
                .cachedIn(viewModelScope)
                .collect {
                _multiSearchResults.value = it
            }
        }
    }
}