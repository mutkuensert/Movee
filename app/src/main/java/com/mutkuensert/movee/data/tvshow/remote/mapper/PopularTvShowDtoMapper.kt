package com.mutkuensert.movee.data.tvshow.remote.mapper

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.tvshow.local.model.PopularTvShowEntity
import com.mutkuensert.movee.data.tvshow.remote.model.PopularTvShowDto
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject

class PopularTvShowDtoMapper @Inject constructor(
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) {
    suspend fun mapToEntity(
        popularTvShowDto: PopularTvShowDto,
        page: Int
    ): PopularTvShowEntity {
        return PopularTvShowEntity(
            id = popularTvShowDto.id,
            page = page,
            posterPath = popularTvShowDto.posterPath,
            title = popularTvShowDto.name,
            voteAverage = popularTvShowDto.voteAverage,
            isFavorite = isFavoriteTvShow(popularTvShowDto.id)
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