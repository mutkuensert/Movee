package com.mutkuensert.movee.data.tvshow.remote.mapper

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.tvshow.local.model.TopRatedTvShowEntity
import com.mutkuensert.movee.data.tvshow.remote.model.TopRatedTvShowDto
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject

class TopRatedTvShowDtoMapper @Inject constructor(
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) {
    suspend fun mapToEntity(
        topRatedTvShowDto: TopRatedTvShowDto,
        page: Int
    ): TopRatedTvShowEntity {
        return TopRatedTvShowEntity(
            id = topRatedTvShowDto.id,
            page = page,
            posterPath = topRatedTvShowDto.posterPath,
            title = topRatedTvShowDto.name,
            voteAverage = topRatedTvShowDto.voteAverage,
            isFavorite = isFavoriteTvShow(topRatedTvShowDto.id)
        )
    }

    private suspend fun isFavoriteTvShow(id: Int): Boolean? {
        return if (sessionManager.isLoggedIn()) {
            accountDao.isTvShowFavorite(id)
        } else {
            null
        }
    }
}