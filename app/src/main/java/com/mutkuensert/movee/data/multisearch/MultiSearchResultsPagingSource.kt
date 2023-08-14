package com.mutkuensert.movee.data.multisearch

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.multisearch.model.MultiSearchResponse
import com.mutkuensert.movee.data.multisearch.model.MultiSearchResultDto
import com.mutkuensert.movee.domain.Failure
import retrofit2.HttpException
import retrofit2.Response

class MultiSearchResultsPagingSource(
    private val getMultiSearchResult: suspend (page: Int) -> Response<MultiSearchResponse>,
) :
    PagingSource<Int, MultiSearchResultDto>() {
    override fun getRefreshKey(state: PagingState<Int, MultiSearchResultDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MultiSearchResultDto> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = getMultiSearchResult.invoke(nextPageNumber)

            return if (response.isSuccessful && response.body() != null) {
                LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = null,
                    nextKey = if (nextPageNumber + 1 <= response.body()!!.totalPages) nextPageNumber + 1 else null
                )
            } else {
                LoadResult.Error(Failure(message = "Unsuccessful Request"))
            }

        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }
}