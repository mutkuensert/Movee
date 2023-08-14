package com.mutkuensert.movee.data.movie.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mutkuensert.movee.data.util.ApiConstants
import com.mutkuensert.movee.data.movie.PopularMoviesResultMapper
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.local.model.PopularMovieEntity
import com.mutkuensert.movee.data.movie.remote.model.PopularMoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val getPopularMovies: suspend (page: Int) -> Response<PopularMoviesResponse>,
    private val movieDao: MovieDao,
    private val popularMoviesResultMapper: PopularMoviesResultMapper,
) : RemoteMediator<Int, PopularMovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { movieDao.clearAllPopularMovies() }
                    ApiConstants.General.DEFAULT_FIRST_PAGE
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        movieDao.getAllPopularMovies().lastOrNull()?.page
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            val popularMoviesResponse = getPopularMovies.invoke(page)

            if (popularMoviesResponse.isSuccessful && !popularMoviesResponse.body()?.results.isNullOrEmpty()) {
                val popularMovies = popularMoviesResponse.body()!!.results
                withContext(Dispatchers.IO) {
                    movieDao.insertAll(
                        *popularMovies.map {
                            popularMoviesResultMapper.mapToEntity(it, page)
                        }.toTypedArray()
                    )
                }
                MediatorResult.Success(endOfPaginationReached = false)
            } else {
                MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}