package movee.domain.movie

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import movee.domain.Failure
import kotlinx.coroutines.flow.Flow
import movee.domain.movie.model.MovieCast
import movee.domain.movie.model.MovieDetails
import movee.domain.movie.model.MovieNowPlaying
import movee.domain.movie.model.PopularMovie

interface MovieRepository {
    fun getPopularMoviesPagingFlow(): Flow<PagingData<PopularMovie>>
    fun getMoviesNowPlayingPagingFlow(): Flow<PagingData<MovieNowPlaying>>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, Failure>
    suspend fun getMovieCast(movieId: Int): Result<List<MovieCast>, Failure>
    suspend fun addMovieToFavorites(movieId: Int)
    suspend fun removeMovieFromFavorites(movieId: Int)
}