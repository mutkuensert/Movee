package movee.domain.account

interface AccountRepository {
    suspend fun fetchUserDetails(): Boolean
    suspend fun fetchFavoriteMoviesAndTvShows()
    suspend fun addMovieToFavorites(isFavorite: Boolean, movieId: Int)
    suspend fun addTvShowToFavorites(isFavorite: Boolean, movieId: Int)
    suspend fun clearAllFavoriteMoviesAndTvShows()
}