package com.mutkuensert.movee.domain.multisearch

import androidx.paging.PagingData
import com.mutkuensert.movee.domain.multisearch.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface MultiSearchRepository {
    suspend fun getSearchFlow(query: String): Flow<PagingData<SearchResult>>
}