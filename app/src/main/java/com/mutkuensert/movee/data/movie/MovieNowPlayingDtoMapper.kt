package com.mutkuensert.movee.data.movie

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.account.local.model.FavoriteMovieEntity
import com.mutkuensert.movee.data.movie.local.model.MovieNowPlayingEntity
import com.mutkuensert.movee.data.movie.remote.model.MovieNowPlayingDto
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject

/**
 * Must not be singleton.
 */
class MovieNowPlayingDtoMapper @Inject constructor(
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) {
    private val favorites = mutableListOf<FavoriteMovieEntity>()
    private var key: Int? = null

    suspend fun mapToEntity(
        movieNowPlayingResult: MovieNowPlayingDto,
        page: Int
    ): MovieNowPlayingEntity {
        if (page != key) {
            favorites.clear()
            favorites.addAll(accountDao.getFavoriteMovies())
            key = page
        }

        return MovieNowPlayingEntity(
            id = movieNowPlayingResult.id,
            page = page,
            posterPath = movieNowPlayingResult.posterPath,
            title = movieNowPlayingResult.title,
            voteAverage = movieNowPlayingResult.voteAverage,
            isFavorite = isMovieFavorite(movieNowPlayingResult.id)
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