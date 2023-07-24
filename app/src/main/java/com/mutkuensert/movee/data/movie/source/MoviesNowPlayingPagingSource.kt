package com.mutkuensert.movee.data.movie.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.movie.model.MoviesNowPlayingModel
import com.mutkuensert.movee.data.movie.model.MoviesNowPlayingResult
import com.mutkuensert.movee.util.UnsuccessfulResponseException
import retrofit2.HttpException
import retrofit2.Response

class MoviesNowPlayingPagingSource(private val getMoviesNowPlaying: suspend (page: Int) -> Response<MoviesNowPlayingModel>) :
    PagingSource<Int, MoviesNowPlayingResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesNowPlayingResult> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = getMoviesNowPlaying.invoke(nextPageNumber)

            return if (response.isSuccessful && response.body() != null) {
                LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = null,
                    nextKey = if (nextPageNumber + 1 <= response.body()!!.totalPages) nextPageNumber + 1 else null
                )
            } else {
                LoadResult.Error(UnsuccessfulResponseException("Unsuccessful Popular Movies Request"))
            }
        } catch (exception: HttpException) {
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