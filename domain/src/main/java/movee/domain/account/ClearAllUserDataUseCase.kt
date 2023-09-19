package movee.domain.account

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movee.domain.library.SessionManager
import movee.domain.library.UserManager

@Singleton
class ClearAllUserDataUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userManager: UserManager,
    private val sessionManager: SessionManager,
) {

    suspend fun execute() {
        withContext(Dispatchers.IO) {
            accountRepository.clearAllFavoriteMoviesAndTvShows()
            userManager.removeCurrentUser()
            sessionManager.removeRequestToken()
            sessionManager.removeSession()
        }
    }
}