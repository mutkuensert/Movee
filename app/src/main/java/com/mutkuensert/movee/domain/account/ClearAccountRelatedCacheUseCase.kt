package com.mutkuensert.movee.domain.account

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.movie.local.MovieDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class ClearAccountRelatedCacheUseCase @Inject constructor(
    private val accountDao: AccountDao,
    private val movieDao: MovieDao,
) {

    suspend fun execute() {
        withContext(Dispatchers.IO) {
            accountDao.clearAllFavoriteMovies()
            movieDao.clearAllPopularMovies()
        }
    }
}