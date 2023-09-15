package movee.data.tvshow.remote.mapper

import javax.inject.Inject
import movee.data.account.local.AccountDao
import movee.data.tvshow.local.model.PopularTvShowEntity
import movee.data.tvshow.remote.model.PopularTvShowDto
import movee.domain.library.SessionManager

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