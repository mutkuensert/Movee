package com.mutkuensert.movee.data.multisearch

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mutkuensert.movee.data.multisearch.model.MovieResultItemDto
import com.mutkuensert.movee.data.multisearch.model.MultiSearchResultDto
import com.mutkuensert.movee.data.multisearch.model.PersonResulItemDto
import com.mutkuensert.movee.data.multisearch.model.TvResultItemDto
import com.mutkuensert.movee.data.util.getImageUrl
import com.mutkuensert.movee.domain.multisearch.MultiSearchRepository
import com.mutkuensert.movee.domain.multisearch.model.SearchResult
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MultiSearchRepositoryImpl @Inject constructor(
    private val multiSearchApi: MultiSearchApi
) : MultiSearchRepository {
    override suspend fun getSearchFlow(query: String): Flow<PagingData<SearchResult>> {
        return if (query.length > 2) {
            delay(1000)

            Pager(
                PagingConfig(pageSize = 20)
            ) {
                MultiSearchResultsPagingSource { page ->
                    multiSearchApi.multiSearch(query, page)
                }
            }.flow.map { pagingData ->
                pagingData.map { dto ->
                    mapSearchResultDto(dto)
                }
            }
        } else {
            MutableStateFlow(PagingData.empty())
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