package com.mutkuensert.movee.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.data.ApiConstants
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.account.local.entity.FavoriteMovieEntity
import com.mutkuensert.movee.data.account.remote.AccountApi
import com.mutkuensert.movee.data.account.remote.model.FavoriteMovieRequestModel
import com.mutkuensert.movee.data.account.remote.model.FavoriteMoviesResultResponseModel
import com.mutkuensert.movee.data.authentication.dto.AccountDetailsDto
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.account.AccountRepository
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
    private val movieDao: MovieDao,
) : AccountRepository {

    override suspend fun getUserDetails(): Result<UserDetails, Failure> {
        val sessionId = sessionManager.getSessionId()
            ?: return Err(Failure(statusMessage = "${SessionManager::getSessionId.name} returned null."))

        return accountApi.getAccountDetails(sessionId).toResult(::mapToUserDetails)
    }

    override suspend fun fetchAndInsertFavoriteMovies() {
        accountDao.clearAllFavoriteMovies()

        for (page in ApiConstants.General.DEFAULT_FIRST_PAGE..4) {
            accountApi.getFavoriteMovies(page).toResult().onSuccess { favoriteMoviesResponseModel ->
                if (favoriteMoviesResponseModel.results.isEmpty() && page > ApiConstants.General.DEFAULT_FIRST_PAGE) {
                    return
                } else if (favoriteMoviesResponseModel.results.isEmpty()) {
                    accountDao.clearAllFavoriteMovies()
                    return
                } else {
                    accountDao.insertFavoriteMovies(
                        *favoriteMoviesResponseModel
                            .results
                            .map(::mapToFavoriteMovieEntity)
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
                FavoriteMovieRequestModel(
                    favorite = true,
                    mediaId = movieId
                )
            ).toResult().onFailure {
                deleteFavoriteFromMovie(movieId)
            }
        } else {
            deleteFavoriteFromMovie(movieId = movieId)

            accountApi.postFavoriteMovie(
                FavoriteMovieRequestModel(
                    favorite = false,
                    mediaId = movieId
                )
            ).toResult().onFailure {
                insertFavoriteMovieInCache(movieId = movieId)
            }
        }
    }

    private suspend fun insertFavoriteMovieInCache(movieId: Int) = withContext(Dispatchers.IO) {
        accountDao.insertFavoriteMovies(FavoriteMovieEntity(movieId))
        movieDao.update(movieDao.getPopularMovie(movieId).copyWithPrimaryKey(isFavorite = true))
    }

    private suspend fun deleteFavoriteFromMovie(movieId: Int) = withContext(Dispatchers.IO) {
        accountDao.deleteFavoriteMovies(FavoriteMovieEntity(movieId))
        movieDao.update(movieDao.getPopularMovie(movieId).copyWithPrimaryKey(isFavorite = false))
    }
}

private fun mapToUserDetails(accountDetailsDto: AccountDetailsDto): UserDetails {
    return UserDetails(
        avatarPath = accountDetailsDto.avatar.tmdb.avatarPath,
        gravatarHash = accountDetailsDto.avatar.gravatar.hash,
        id = accountDetailsDto.id,
        includeAdult = accountDetailsDto.includeAdult,
        iso_3166_1 = accountDetailsDto.iso_3166_1,
        iso_639_1 = accountDetailsDto.iso_639_1,
        name = accountDetailsDto.name,
        username = accountDetailsDto.username
    )
}

private fun mapToFavoriteMovieEntity(
    favoriteMoviesResultResponseModel: FavoriteMoviesResultResponseModel
): FavoriteMovieEntity {
    return FavoriteMovieEntity(id = favoriteMoviesResultResponseModel.id)
}