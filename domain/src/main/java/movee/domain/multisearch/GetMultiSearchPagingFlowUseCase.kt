package movee.domain.multisearch

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import movee.domain.multisearch.model.SearchResult

class GetMultiSearchPagingFlowUseCase @Inject constructor(
    private val multiSearchRepository: MultiSearchRepository
) {

    suspend fun execute(query: String): Flow<PagingData<SearchResult>> {
        return if (query.length > 2) {
            delay(1000)

            multiSearchRepository.getSearchPagingFlow(query = query)
        } else {
            MutableStateFlow(PagingData.empty())
        }
    }
}