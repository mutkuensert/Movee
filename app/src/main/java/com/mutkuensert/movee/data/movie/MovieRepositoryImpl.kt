package com.mutkuensert.movee.data.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.remote.mediator.PopularMoviesRemoteMediator
import com.mutkuensert.movee.domain.movie.MovieRepository
import com.mutkuensert.movee.domain.movie.model.PopularMovie
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularMoviesFlow(): Flow<PagingData<PopularMovie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PopularMoviesRemoteMediator(
                getPopularMovies = movieApi::getPopularMovies,
                movieDao = movieDao
            ),
            pagingSourceFactory = { movieDao.getPopularMoviesPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                PopularMovie(
                    posterPath = it.posterPath,
                    title = it.title,
                    id = it.id,
                    isFavorite = null,
                    voteAverage = it.voteAverage
                )
            }
        }
    }
}