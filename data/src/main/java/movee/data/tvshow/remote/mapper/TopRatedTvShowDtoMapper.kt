package movee.data.tvshow.remote.mapper

import javax.inject.Inject
import movee.data.account.local.AccountDao
import movee.data.tvshow.local.model.TopRatedTvShowEntity
import movee.data.tvshow.remote.model.TopRatedTvShowDto
import movee.domain.library.SessionManager

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