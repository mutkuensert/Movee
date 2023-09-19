package movee.domain.login

import javax.inject.Inject
import javax.inject.Singleton
import movee.domain.account.ClearAllUserDataUseCase

@Singleton
class LogoutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val clearAllUserDataUseCase: ClearAllUserDataUseCase,
) {

    suspend fun execute(): Boolean {
        val isLoggedOut = authenticationRepository.logout()

        if (isLoggedOut) {
            clearAllUserDataUseCase.execute()

            return true
        }

        return false
    }
}