package com.mutkuensert.movee.data.datasource.paging.multisearch

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.api.MultiSearchApi
import com.mutkuensert.movee.data.model.remote.search.MultiSearchResultMediaType
import com.mutkuensert.movee.util.CustomUnsuccessfulResponseException
import retrofit2.HttpException

class MultiSearchResultsPagingSource(val multiSearchApi: MultiSearchApi, val searchQuery: String): PagingSource<Int, MultiSearchResultMediaType>() {
    override fun getRefreshKey(state: PagingState<Int, MultiSearchResultMediaType>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MultiSearchResultMediaType> {

        try {
            if(searchQuery == "") return LoadResult.Page(data = listOf(), prevKey = null, nextKey = null)

            val nextPageNumber = params.key ?: 1
            val response = multiSearchApi.multiSearch(query = searchQuery, page = nextPageNumber)

            if (response.isSuccessful && response.body() != null) {
                return LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = null,
                    nextKey = if (nextPageNumber + 1 <= response.body()!!.totalPages) nextPageNumber + 1 else null
                )
            } else {
                return LoadResult.Error(CustomUnsuccessfulResponseException("Unsuccessful Multi Search Request"))
            }

        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }
}