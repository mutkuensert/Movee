package com.mutkuensert.movee.data.tvshow.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.tvshow.model.PopularTvShowDto
import com.mutkuensert.movee.data.tvshow.model.PopularTvShowsResponse
import com.mutkuensert.movee.util.UnsuccessfulResponseException
import retrofit2.HttpException
import retrofit2.Response

class PopularTvShowsPagingSource(private val getPopularTvShows: suspend (page: Int) -> Response<PopularTvShowsResponse>) :
    PagingSource<Int, PopularTvShowDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularTvShowDto> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = getPopularTvShows.invoke(nextPageNumber)

            return if (response.isSuccessful && response.body() != null) {
                LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = null,
                    nextKey = if (nextPageNumber + 1 <= response.body()!!.totalPages) nextPageNumber + 1 else null
                )
            } else {
                LoadResult.Error(UnsuccessfulResponseException("Unsuccessful Popular Tv Shows Request"))
            }
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PopularTvShowDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}