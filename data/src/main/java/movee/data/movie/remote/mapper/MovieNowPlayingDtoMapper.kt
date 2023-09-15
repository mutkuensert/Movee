package movee.data.movie.remote.mapper

import javax.inject.Inject
import movee.data.account.local.AccountDao
import movee.data.movie.local.model.MovieNowPlayingEntity
import movee.data.movie.remote.model.MovieNowPlayingDto
import movee.domain.library.SessionManager

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