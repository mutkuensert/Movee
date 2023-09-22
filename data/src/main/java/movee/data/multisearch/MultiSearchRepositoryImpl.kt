package movee.data.multisearch

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import movee.data.multisearch.model.MovieResultItemDto
import movee.data.multisearch.model.MultiSearchResultDto
import movee.data.multisearch.model.PersonResulItemDto
import movee.data.multisearch.model.TvResultItemDto
import movee.data.util.getImageUrl
import movee.domain.multisearch.MultiSearchRepository
import movee.domain.multisearch.model.SearchResult

class MultiSearchRepositoryImpl @Inject constructor(
    private val multiSearchApi: MultiSearchApi
) : MultiSearchRepository {
    override suspend fun getSearchPagingFlow(query: String): Flow<PagingData<SearchResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MultiSearchResultsPagingSource { page ->
                    multiSearchApi.multiSearch(query, page)
                }
            }
        ).flow.map { pagingData ->
            pagingData.map { dto ->
                mapSearchResultDto(dto)
            }
        }
    }

    private fun mapSearchResultDto(multiSearchResultDto: MultiSearchResultDto): SearchResult {
        return when (multiSearchResultDto) {
            is PersonResulItemDto -> {
                SearchResult(
                    imageUrl = getImageUrl(multiSearchResultDto.profilePath),
                    title = multiSearchResultDto.name,
                    id = multiSearchResultDto.id,
                    type = SearchResult.Type.PERSON
                )
            }

            is MovieResultItemDto -> {
                SearchResult(
                    imageUrl = getImageUrl(multiSearchResultDto.posterPath),
                    title = multiSearchResultDto.title,
                    id = multiSearchResultDto.id,
                    type = SearchResult.Type.MOVIE
                )
            }

            else -> {
                val tvShowDto = multiSearchResultDto as TvResultItemDto

                SearchResult(
                    imageUrl = getImageUrl(tvShowDto.posterPath),
                    title = tvShowDto.name,
                    id = tvShowDto.id,
                    type = SearchResult.Type.TV
                )
            }
        }
    }
}