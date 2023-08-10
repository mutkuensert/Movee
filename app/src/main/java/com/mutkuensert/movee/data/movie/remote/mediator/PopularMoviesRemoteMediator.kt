package com.mutkuensert.movee.data.movie.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mutkuensert.movee.data.ApiConstants
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.account.local.entity.FavoriteMovieEntity
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.local.entity.PopularMovieEntity
import com.mutkuensert.movee.data.movie.remote.model.PopularMoviesResponseModel
import com.mutkuensert.movee.data.movie.remote.model.PopularMoviesResultResponseModel
import com.mutkuensert.movee.library.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val getPopularMovies: suspend (page: Int) -> Response<PopularMoviesResponseModel>,
    private val movieDao: MovieDao,
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
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
                    val favorites = accountDao.getFavoriteMovies()
                    val isLoggedIn = sessionManager.isLoggedIn()

                    movieDao.insertAll(*popularMovies.map {
                        it.mapToEntity(
                            page = page, isFavorite = if (isLoggedIn) {
                                favorites.contains(
                                    FavoriteMovieEntity(id = it.id)
                                )
                            } else null
                        )
                    }.toTypedArray())
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

    private fun PopularMoviesResultResponseModel.mapToEntity(
        page: Int,
        isFavorite: Boolean?
    ): PopularMovieEntity {
        return PopularMovieEntity(
            id = id,
            page = page,
            posterPath = posterPath,
            title = title,
            voteAverage = voteAverage,
            isFavorite = isFavorite
        )
    }
}