package movee.domain.login

import javax.inject.Inject
import javax.inject.Singleton
import movee.domain.account.ClearAccountRelatedCacheUseCase
import movee.domain.library.UserManager

@Singleton
class LogoutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userManager: UserManager,
    private val clearAccountRelatedCacheUseCase: ClearAccountRelatedCacheUseCase,
) {

    suspend fun execute(): Boolean {
        val isLoggedOut = authenticationRepository.logout()

        if (isLoggedOut) {
            userManager.removeCurrentUser()
            clearAccountRelatedCacheUseCase.execute()

            return true
        }

        return false
    }
}