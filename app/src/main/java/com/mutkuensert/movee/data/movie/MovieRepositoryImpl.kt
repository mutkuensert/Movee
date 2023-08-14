package com.mutkuensert.movee.data.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.michaelbull.result.Result
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.remote.mediator.PopularMoviesRemoteMediator
import com.mutkuensert.movee.data.movie.source.MoviesNowPlayingPagingSource
import com.mutkuensert.movee.data.util.getImageUrl
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.movie.MovieRepository
import com.mutkuensert.movee.domain.movie.model.MovieCast
import com.mutkuensert.movee.domain.movie.model.MovieDetails
import com.mutkuensert.movee.domain.movie.model.MovieNowPlaying
import com.mutkuensert.movee.domain.movie.model.PopularMovie
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
    private val popularMoviesResultMapper: PopularMoviesResultMapper,
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularMoviesPagingFlow(): Flow<PagingData<PopularMovie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PopularMoviesRemoteMediator(
                getPopularMovies = movieApi::getPopularMovies,
                movieDao = movieDao,
                popularMoviesResultMapper = popularMoviesResultMapper
            ),
            pagingSourceFactory = { movieDao.getPopularMoviesPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                PopularMovie(
                    imageUrl = getImageUrl(it.posterPath),
                    title = it.title,
                    id = it.id,
                    isFavorite = it.isFavorite,
                    voteAverage = it.voteAverage
                )
            }
        }
    }

    override fun getMoviesNowPlayingPagingFlow(): Flow<PagingData<MovieNowPlaying>> {
        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            MoviesNowPlayingPagingSource(movieApi::getMoviesNowPlaying)
        }.flow.map { pagingData ->
            pagingData.map {
                MovieNowPlaying(
                    imageUrl = getImageUrl(it.posterPath),
                    title = it.title,
                    id = it.id,
                    voteAverage = it.voteAverage
                )
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, Failure> {
        return movieApi.getMovieDetails(movieId).toResult(mapper = {
            MovieDetails(
                imageUrl = getImageUrl(it.posterPath),
                title = it.title,
                voteAverage = it.voteAverage,
                runtime = it.runtime,
                overview = it.overview
            )
        })
    }

    override suspend fun getMovieCast(movieId: Int): Result<List<MovieCast>, Failure> {
        return movieApi.getMovieCredits(movieId).toResult(mapper = { response ->
            response.cast.map {
                MovieCast(
                    id = it.id,
                    imageUrl = getImageUrl(it.profilePath),
                    name = it.name,
                    character = it.character
                )
            }
        })
    }
}