package com.mutkuensert.movee.data.movie.remote.mapper

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.movie.local.model.PopularMovieEntity
import com.mutkuensert.movee.data.movie.remote.model.PopularMovieDto
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject

class PopularMovieDtoMapper @Inject constructor(
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) {
    suspend fun mapToEntity(
        popularMovieDto: PopularMovieDto,
        page: Int
    ): PopularMovieEntity {
        return PopularMovieEntity(
            id = popularMovieDto.id,
            page = page,
            posterPath = popularMovieDto.posterPath,
            title = popularMovieDto.title,
            voteAverage = popularMovieDto.voteAverage,
            isFavorite = isMovieFavorite(popularMovieDto.id)
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