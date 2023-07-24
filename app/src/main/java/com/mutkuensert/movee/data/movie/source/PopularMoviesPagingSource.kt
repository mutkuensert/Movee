package com.mutkuensert.movee.data.movie.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.movie.model.PopularMoviesModel
import com.mutkuensert.movee.data.movie.model.PopularMoviesResult
import com.mutkuensert.movee.util.UnsuccessfulResponseException
import retrofit2.HttpException
import retrofit2.Response

class PopularMoviesPagingSource(private val getPopularMovies: suspend (page: Int) -> Response<PopularMoviesModel>) :
    PagingSource<Int, PopularMoviesResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMoviesResult> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = getPopularMovies.invoke(nextPageNumber)

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

    override fun getRefreshKey(state: PagingState<Int, PopularMoviesResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}