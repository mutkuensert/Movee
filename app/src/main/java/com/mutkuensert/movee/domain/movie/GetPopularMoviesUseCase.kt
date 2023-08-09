package com.mutkuensert.movee.domain.movie

import androidx.paging.PagingData
import androidx.paging.map
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.domain.account.model.FavoriteMovie
import com.mutkuensert.movee.domain.movie.model.PopularMovie
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val accountDao: AccountDao,
    private val sessionManager: SessionManager,
) {
    private val favoriteMovies = mutableListOf<FavoriteMovie>()
    suspend fun execute(): Flow<PagingData<PopularMovie>> {
        favoriteMovies.clear()
        favoriteMovies.addAll(accountDao.getFavoriteMovies().map { it.toFavoriteMovie() })

        return movieRepository.getPopularMoviesFlow().map { pagingData ->
            pagingData.map {
                it.copy(isFavorite = getIsFavoriteIfLoggedIn(movieId = it.id))
            }
        }
    }

    private fun getIsFavoriteIfLoggedIn(movieId: Int): Boolean? {
        return if (sessionManager.isLoggedIn()) {
            favoriteMovies.contains(FavoriteMovie(id = movieId))
        } else {
            null
        }
    }
}