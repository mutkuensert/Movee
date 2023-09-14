package com.mutkuensert.movee.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.account.local.model.FavoriteMovieEntity
import com.mutkuensert.movee.data.account.local.model.FavoriteTvShowEntity
import com.mutkuensert.movee.data.account.remote.AccountApi
import com.mutkuensert.movee.data.account.remote.model.FavoriteMovieDto
import com.mutkuensert.movee.data.account.remote.model.FavoriteMoviesResultsDto
import com.mutkuensert.movee.data.account.remote.model.FavoriteTvShowDto
import com.mutkuensert.movee.data.account.remote.model.FavoriteTvShowsResultsDto
import com.mutkuensert.movee.data.authentication.model.AccountDetailsResponse
import com.mutkuensert.movee.data.util.ApiConstants
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.domain.movie.MovieRepository
import com.mutkuensert.movee.domain.tvshow.TvShowRepository
import com.mutkuensert.movee.library.session.SessionManager
import com.mutkuensert.movee.library.user.UserDetails
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl @Inject constructor(
    private val accountApi: AccountApi,
    private val sessionManager: SessionManager,
    private val accountDao: AccountDao,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository,
) : AccountRepository {

    override suspend fun getUserDetails(): Result<UserDetails, Failure> {
        val sessionId = sessionManager.getSessionId()
            ?: return Err(Failure(message = "${SessionManager::getSessionId.name} returned null."))

        return accountApi.getAccountDetails(sessionId).toResult(::mapToUserDetails)
    }

    override suspend fun fetchFavoriteMoviesAndTvShows() {
        accountDao.clearAllFavoriteMovies()

        for (page in ApiConstants.General.DEFAULT_FIRST_PAGE..4) {
            accountApi.getFavoriteMovies(page).toResult().onSuccess { favoriteMoviesResponse ->
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

            accountApi.getFavoriteTvShows(page).toResult().onSuccess { favoriteTvShowsResponse ->
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

    override suspend fun addMovieToFavorites(isFavorite: Boolean, movieId: Int) {
        if (isFavorite) {
            insertFavoriteMovieInCache(movieId)

            accountApi.postFavoriteMovie(
                FavoriteMovieDto(
                    favorite = true,
                    mediaId = movieId
                )
            ).toResult().onFailure {
                deleteFavoriteMovie(movieId)
            }
        } else {
            deleteFavoriteMovie(movieId = movieId)

            accountApi.postFavoriteMovie(
                FavoriteMovieDto(
                    favorite = false,
                    mediaId = movieId
                )
            ).toResult().onFailure {
                insertFavoriteMovieInCache(movieId = movieId)
            }
        }
    }

    override suspend fun addTvShowToFavorites(isFavorite: Boolean, movieId: Int) {
        if (isFavorite) {
            insertFavoriteTvShowInCache(movieId)

            accountApi.postFavoriteTvShow(
                FavoriteTvShowDto(
                    favorite = true,
                    mediaId = movieId
                )
            ).toResult().onFailure {
                deleteFavoriteTvShow(movieId)
            }
        } else {
            deleteFavoriteTvShow(tvShowId = movieId)

            accountApi.postFavoriteTvShow(
                FavoriteTvShowDto(
                    favorite = false,
                    mediaId = movieId
                )
            ).toResult().onFailure {
                insertFavoriteTvShowInCache(tvShowId = movieId)
            }
        }
    }

    private suspend fun insertFavoriteMovieInCache(movieId: Int) = withContext(Dispatchers.IO) {
        accountDao.insertFavoriteMovies(FavoriteMovieEntity(movieId))
        movieRepository.addMovieToFavorites(movieId)
    }

    private suspend fun deleteFavoriteMovie(movieId: Int) = withContext(Dispatchers.IO) {
        accountDao.deleteFavoriteMovies(FavoriteMovieEntity(movieId))
        movieRepository.removeMovieFromFavorites(movieId)
    }

    private suspend fun insertFavoriteTvShowInCache(tvShowId: Int) = withContext(Dispatchers.IO) {
        accountDao.insertFavoriteTvShows(FavoriteTvShowEntity(tvShowId))
        tvShowRepository.addTvShowToFavorites(tvShowId)
    }

    private suspend fun deleteFavoriteTvShow(tvShowId: Int) = withContext(Dispatchers.IO) {
        accountDao.deleteFavoriteTvShows(FavoriteTvShowEntity(tvShowId))
        tvShowRepository.removeTvShowFromFavorites(tvShowId)
    }
}

private fun mapToUserDetails(accountDetailsResponse: AccountDetailsResponse): UserDetails {
    return UserDetails(
        avatarPath = accountDetailsResponse.avatar.tmdb.avatarPath,
        gravatarHash = accountDetailsResponse.avatar.gravatar.hash,
        id = accountDetailsResponse.id,
        includeAdult = accountDetailsResponse.includeAdult,
        iso_3166_1 = accountDetailsResponse.iso_3166_1,
        iso_639_1 = accountDetailsResponse.iso_639_1,
        name = accountDetailsResponse.name,
        username = accountDetailsResponse.username
    )
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