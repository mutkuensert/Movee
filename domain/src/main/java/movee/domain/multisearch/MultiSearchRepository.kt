package movee.domain.multisearch

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import movee.domain.multisearch.model.SearchResult

interface MultiSearchRepository {
    suspend fun getSearchFlow(query: String): Flow<PagingData<SearchResult>>
}