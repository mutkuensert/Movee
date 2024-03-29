package movee.data.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import movee.data.movie.local.MovieDao
import movee.data.movie.remote.mapper.MovieNowPlayingDtoMapper
import movee.data.movie.remote.mapper.PopularMovieDtoMapper
import movee.data.movie.remote.mediator.MoviesNowPlayingRemoteMediator
import movee.data.movie.remote.mediator.PopularMoviesRemoteMediator
import movee.data.network.toResult
import movee.data.util.getImageUrl
import movee.data.util.withDecimalDigits
import movee.domain.Failure
import movee.domain.movie.MovieRepository
import movee.domain.movie.model.Person
import movee.domain.movie.model.MovieDetails
import movee.domain.movie.model.MovieNowPlaying
import movee.domain.movie.model.PopularMovie

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
    private val popularMovieDtoMapper: PopularMovieDtoMapper,
    private val movieNowPlayingDtoMapper: MovieNowPlayingDtoMapper,
) : MovieRepository {

    override fun getPopularMoviesPagingFlow(): Flow<PagingData<PopularMovie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PopularMoviesRemoteMediator(
                getPopularMovies = movieApi::getPopularMovies,
                movieDao = movieDao,
                popularMovieDtoMapper = popularMovieDtoMapper
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
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesNowPlayingRemoteMediator(
                getMoviesNowPlaying = movieApi::getMoviesNowPlaying,
                movieDao = movieDao,
                movieNowPlayingDtoMapper = movieNowPlayingDtoMapper
            ),
            pagingSourceFactory = { movieDao.getMoviesNowPlayingPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                MovieNowPlaying(
                    imageUrl = getImageUrl(it.posterPath),
                    title = it.title,
                    id = it.id,
                    voteAverage = it.voteAverage,
                    isFavorite = it.isFavorite
                )
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, Failure> {
        return movieApi.getMovieDetails(movieId).toResult(mapper = {
            MovieDetails(
                imageUrl = getImageUrl(it.posterPath),
                title = it.title,
                voteAverage = it.voteAverage.withDecimalDigits(1),
                runtime = it.runtime,
                overview = it.overview
            )
        })
    }

    override suspend fun getMovieCast(movieId: Int): Result<List<Person>, Failure> {
        return movieApi.getMovieCredits(movieId).toResult(mapper = { response ->
            response.cast.map {
                Person(
                    id = it.id,
                    imageUrl = getImageUrl(it.profilePath),
                    name = it.name,
                    character = it.character
                )
            }
        })
    }

    override suspend fun addMovieToFavorites(movieId: Int) {
        movieDao.getPopularMovie(movieId)
            ?.copyWithPrimaryKey(isFavorite = true)
            ?.let {
                movieDao.update(it)
            }

        movieDao.getMovieNowPlaying(movieId)
            ?.copyWithPrimaryKey(isFavorite = true)
            ?.let {
                movieDao.update(it)
            }
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        movieDao.getPopularMovie(movieId)
            ?.copyWithPrimaryKey(isFavorite = false)
            ?.let {
                movieDao.update(it)
            }

        movieDao.getMovieNowPlaying(movieId)
            ?.copyWithPrimaryKey(isFavorite = false)
            ?.let {
                movieDao.update(it)
            }
    }
}