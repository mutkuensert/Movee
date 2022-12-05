package com.mutkuensert.movee.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.MoviesNowPlayingResult
import com.mutkuensert.movee.util.CustomUnsuccessfulResponseException
import retrofit2.HttpException

class MoviesNowPlayingPagingSource(val requestService: RequestService): PagingSource<Int, MoviesNowPlayingResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesNowPlayingResult> {
        try {

            val nextPageNumber = params.key?: 1
            val response = requestService.getMoviesNowPlaying(page = nextPageNumber)

            if(response.isSuccessful && response.body() != null){
                return LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = null,
                    nextKey = if (nextPageNumber + 1 <= response.body()!!.totalPages) nextPageNumber + 1 else null
                )
            }else {
                return LoadResult.Error(CustomUnsuccessfulResponseException("Unsuccessful Popular Movies Request"))
            }

        }catch (exception: HttpException){
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesNowPlayingResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}