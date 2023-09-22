package movee.data.account

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movee.data.account.local.AccountDao
import movee.data.account.local.model.FavoriteMovieEntity
import movee.data.account.local.model.FavoriteTvShowEntity
import movee.data.account.remote.AccountApi
import movee.data.account.remote.model.FavoriteMovieDto
import movee.data.account.remote.model.FavoriteMoviesResultsDto
import movee.data.account.remote.model.FavoriteTvShowDto
import movee.data.account.remote.model.FavoriteTvShowsResultsDto
import movee.data.network.toEmptyResult
import movee.data.network.toResult
import movee.data.util.ApiConstants
import movee.domain.Failure
import movee.domain.account.AccountRepository
import movee.domain.library.SessionManager
import movee.domain.library.UserManager
import movee.domain.movie.MovieRepository
import movee.domain.tvshow.TvShowRepository
import timber.log.Timber

class AccountRepositoryImpl @Inject constructor(
    private val accountApi: AccountApi,
    private val sessionManager: SessionManager,
    private val userManager: UserManager,
    private val accountDao: AccountDao,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository,
) : AccountRepository {

    override suspend fun fetchUserDetails(): Boolean {
        val sessionId = sessionManager.getSessionId()
            ?: run {
                Timber.e("${sessionManager::getSessionId.name} returned null.")
                return false
            }

        accountApi.getAccountDetails(sessionId).toResult().onSuccess {
            userManager.setCurrentUserId(it.id)
            fetchFavoriteMoviesAndTvShows()

            return true
        }
            .onFailure {
                Timber.e(it.message)
            }

        return false
    }

    override suspend fun fetchFavoriteMoviesAndTvShows() {
        clearAllFavoriteMoviesAndTvShowsInCache()

        for (page in ApiConstants.General.DEFAULT_FIRST_PAGE..4) {
            accountApi.getFavoriteMovies(
                page = page,
                sessionId = sessionManager.getSessionId()!!
            ).toResult().onSuccess { favoriteMoviesResponse ->
                if (favoriteMoviesResponse.results.isEmpty() && page > ApiConstants.General.DEFAULT_FIRST_PAGE) {
                    return
                } else if (favoriteMoviesResponse.results.isEmpty()) {
                    accountDao.clearAllFavoriteMovies()
                    return
                } else {
                    accountDao.insertFavoriteMovies(
                        *favoriteMoviesResponse
                            .results
                            .map(::mapToFavoriteMovieEntity)
                            .toTypedArray()
                    )
                }
            }.onFailure {
                return
            }

            accountApi.getFavoriteTvShows(
                page = page,
                sessionId = sessionManager.getSessionId()!!
            ).toResult().onSuccess { favoriteTvShowsResponse ->
                if (favoriteTvShowsResponse.results.isEmpty() && page > ApiConstants.General.DEFAULT_FIRST_PAGE) {
                    return
                } else if (favoriteTvShowsResponse.results.isEmpty()) {
                    accountDao.clearAllFavoriteTvShows()
                    return
                } else {
                    accountDao.insertFavoriteTvShows(
                        *favoriteTvShowsResponse
                            .results
                            .map(::mapToFavoriteTvShowEntity)
                            .toTypedArray()
                    )
                }
            }.onFailure {
                return
            }
        }
    }

    override suspend fun addMovieToFavorites(
        isFavorite: Boolean,
        movieId: Int
    ): Result<Unit, Failure> {
        return accountApi.postFavoriteMovie(
            FavoriteMovieDto(
                favorite = isFavorite,
                mediaId = movieId
            ),
            sessionId = sessionManager.getSessionId()!!
        ).toEmptyResult()
    }

    override suspend fun addTvShowToFavorites(
        isFavorite: Boolean,
        tvShowId: Int
    ): Result<Unit, Failure> {
        return accountApi.postFavoriteTvShow(
            FavoriteTvShowDto(
                favorite = isFavorite,
                mediaId = tvShowId
            ),
            sessionId = sessionManager.getSessionId()!!
        ).toEmptyResult()
    }

    override suspend fun clearAllFavoriteMoviesAndTvShowsInCache() {
        accountDao.clearAllFavoriteMovies()
        accountDao.clearAllFavoriteTvShows()
    }

    override suspend fun insertFavoriteMovieInCache(movieId: Int) = withContext(Dispatchers.IO) {
        accountDao.insertFavoriteMovies(FavoriteMovieEntity(movieId))
        movieRepository.addMovieToFavorites(movieId)
    }

    override suspend fun deleteFavoriteMovieInCache(movieId: Int) = withContext(Dispatchers.IO) {
        accountDao.deleteFavoriteMovies(FavoriteMovieEntity(movieId))
        movieRepository.removeMovieFromFavorites(movieId)
    }

    override suspend fun insertFavoriteTvShowInCache(tvShowId: Int) = withContext(Dispatchers.IO) {
        accountDao.insertFavoriteTvShows(FavoriteTvShowEntity(tvShowId))
        tvShowRepository.addTvShowToFavorites(tvShowId)
    }

    override suspend fun deleteFavoriteTvShowInCache(tvShowId: Int) = withContext(Dispatchers.IO) {
        accountDao.deleteFavoriteTvShows(FavoriteTvShowEntity(tvShowId))
        tvShowRepository.removeTvShowFromFavorites(tvShowId)
    }
}

private fun mapToFavoriteMovieEntity(
    dto: FavoriteMoviesResultsDto
): FavoriteMovieEntity {
    return FavoriteMovieEntity(id = dto.id)
}

private fun mapToFavoriteTvShowEntity(
    dto: FavoriteTvShowsResultsDto
): FavoriteTvShowEntity {
    return FavoriteTvShowEntity(id = dto.id)
}