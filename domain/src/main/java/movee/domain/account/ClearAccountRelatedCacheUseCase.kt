package movee.domain.account

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class ClearAccountRelatedCacheUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {

    suspend fun execute() {
        withContext(Dispatchers.IO) {
            accountRepository.clearAllFavoriteMoviesAndTvShows()
        }
    }
}