package com.mutkuensert.movee.data.movie.remote.mapper

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.movie.local.model.MovieNowPlayingEntity
import com.mutkuensert.movee.data.movie.remote.model.MovieNowPlayingDto
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject

class MovieNowPlayingDtoMapper @Inject constructor(
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) {

    suspend fun mapToEntity(
        movieNowPlayingDto: MovieNowPlayingDto,
        page: Int
    ): MovieNowPlayingEntity {
        return MovieNowPlayingEntity(
            id = movieNowPlayingDto.id,
            page = page,
            posterPath = movieNowPlayingDto.posterPath,
            title = movieNowPlayingDto.title,
            voteAverage = movieNowPlayingDto.voteAverage,
            isFavorite = isMovieFavorite(movieNowPlayingDto.id)
        )
    }

    private suspend fun isMovieFavorite(id: Int): Boolean? {
        return if (sessionManager.isLoggedIn()) {
            accountDao.isMovieFavorite(id)
        } else {
            null
        }
    }
}