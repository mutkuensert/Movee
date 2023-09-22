package movee.domain.account

import com.github.michaelbull.result.Result
import movee.domain.Failure

interface AccountRepository {
    suspend fun fetchUserDetails(): Boolean
    suspend fun fetchFavoriteMoviesAndTvShows()
    suspend fun addMovieToFavorites(isFavorite: Boolean, movieId: Int): Result<Unit, Failure>
    suspend fun addTvShowToFavorites(isFavorite: Boolean, tvShowId: Int): Result<Unit, Failure>
    suspend fun insertFavoriteMovieInCache(movieId: Int)
    suspend fun deleteFavoriteMovieInCache(movieId: Int)
    suspend fun insertFavoriteTvShowInCache(tvShowId: Int)
    suspend fun deleteFavoriteTvShowInCache(tvShowId: Int)
    suspend fun clearAllFavoriteMoviesAndTvShowsInCache()
}