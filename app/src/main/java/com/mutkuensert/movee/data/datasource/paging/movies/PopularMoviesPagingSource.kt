package com.mutkuensert.movee.data.datasource.paging.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mutkuensert.movee.data.api.MovieApi
import com.mutkuensert.movee.data.model.remote.movies.PopularMoviesResult
import com.mutkuensert.movee.util.CustomUnsuccessfulResponseException
import retrofit2.HttpException

class PopularMoviesPagingSource(val movieApi: MovieApi) : PagingSource<Int, PopularMoviesResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMoviesResult> {
        try {

            val nextPageNumber = params.key ?: 1
            val response = movieApi.getPopularMovies(page = nextPageNumber)

            if (response.isSuccessful && response.body() != null) {
                return LoadResult.Page(
                    data = response.body()!!.results,
                    prevKey = null,
                    nextKey = if (nextPageNumber + 1 <= response.body()!!.totalPages) nextPageNumber + 1 else null
                )
            } else {
                return LoadResult.Error(CustomUnsuccessfulResponseException("Unsuccessful Popular Movies Request"))
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