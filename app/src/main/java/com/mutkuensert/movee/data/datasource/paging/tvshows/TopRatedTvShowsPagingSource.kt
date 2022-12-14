package com.mutkuensert.movee.data.datasource.paging.tvshows

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.api.TvShowsApi
import com.mutkuensert.movee.data.model.remote.tvshows.TopRatedTvShowsResult
import com.mutkuensert.movee.util.CustomUnsuccessfulResponseException
import retrofit2.HttpException

class TopRatedTvShowsPagingSource(val tvShowsApi: TvShowsApi) :
    PagingSource<Int, TopRatedTvShowsResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopRatedTvShowsResult> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = tvShowsApi.getTopRatedTvShows(page = nextPageNumber)

            if (response.isSuccessful && response.body() != null) {
                return LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = null,
                    nextKey = if (nextPageNumber + 1 <= response.body()!!.totalPages) nextPageNumber + 1 else null
                )
            } else {
                return LoadResult.Error(CustomUnsuccessfulResponseException("Unsuccessful Top Rated Tv Shows Request"))
            }
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TopRatedTvShowsResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}