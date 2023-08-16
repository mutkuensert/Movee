package com.mutkuensert.movee.data.movie.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mutkuensert.movee.data.movie.MovieNowPlayingDtoMapper
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.local.model.MovieNowPlayingEntity
import com.mutkuensert.movee.data.movie.remote.model.MoviesNowPlayingResponse
import com.mutkuensert.movee.data.util.ApiConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MoviesNowPlayingRemoteMediator(
    private val getMoviesNowPlaying: suspend (page: Int) -> Response<MoviesNowPlayingResponse>,
    private val movieDao: MovieDao,
    private val movieNowPlayingDtoMapper: MovieNowPlayingDtoMapper,
) : RemoteMediator<Int, MovieNowPlayingEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieNowPlayingEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) { movieDao.clearAllMoviesNowPlaying() }
                    ApiConstants.General.DEFAULT_FIRST_PAGE
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastPageNumber = withContext(Dispatchers.IO) {
                        movieDao.getAllMoviesNowPlaying().lastOrNull()?.page
                    }

                    if (lastPageNumber == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastPageNumber + 1
                    }
                }
            }

            val moviesNowPlayingResponse = getMoviesNowPlaying.invoke(page)

            if (moviesNowPlayingResponse.isSuccessful && !moviesNowPlayingResponse.body()?.results.isNullOrEmpty()) {
                val moviesNowPlaying = moviesNowPlayingResponse.body()!!.results
                withContext(Dispatchers.IO) {
                    movieDao.insertMoviesNowPlaying(
                        moviesNowPlaying.map {
                            movieNowPlayingDtoMapper.mapToEntity(it, page)
                        }
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