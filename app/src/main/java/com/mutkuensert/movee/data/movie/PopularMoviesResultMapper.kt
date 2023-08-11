package com.mutkuensert.movee.data.movie

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.account.local.entity.FavoriteMovieEntity
import com.mutkuensert.movee.data.movie.local.entity.PopularMovieEntity
import com.mutkuensert.movee.data.movie.remote.model.PopularMoviesResultResponseModel
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject

/**
 * Must not be singleton.
 */
class PopularMoviesResultMapper @Inject constructor(
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) {
    private val favorites = mutableListOf<FavoriteMovieEntity>()
    private var key: Int? = null

    suspend fun mapToEntity(
        popularMovieResult: PopularMoviesResultResponseModel,
        page: Int
    ): PopularMovieEntity {
        if (page != key) {
            favorites.clear()
            favorites.addAll(accountDao.getFavoriteMovies())
            key = page
        }

        return PopularMovieEntity(
            id = popularMovieResult.id,
            page = page,
            posterPath = popularMovieResult.posterPath,
            title = popularMovieResult.title,
            voteAverage = popularMovieResult.voteAverage,
            isFavorite = isMovieFavorite(popularMovieResult.id)
        )
    }

    private fun isMovieFavorite(id: Int): Boolean? {
        return if (sessionManager.isLoggedIn()) {
            favorites.contains(FavoriteMovieEntity(id))
        } else {
            null
        }
    }
}