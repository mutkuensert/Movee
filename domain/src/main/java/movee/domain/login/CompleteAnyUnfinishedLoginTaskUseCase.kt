package movee.domain.login

import javax.inject.Inject
import javax.inject.Singleton
import movee.domain.account.FetchAccountDetailsUseCase
import movee.domain.library.SessionManager
import movee.domain.library.UserManager
import timber.log.Timber

@Singleton
class CompleteAnyUnfinishedLoginTaskUseCase @Inject constructor(
    private val fetchAccountDetailsUseCase: FetchAccountDetailsUseCase,
    private val userManager: UserManager,
    private val sessionManager: SessionManager,
    private val logoutUseCase: LogoutUseCase,
    private val completeLoginUseCase: CompleteLoginUseCase,
) {

    suspend fun execute() {
        if (!sessionManager.isLoggedIn()) {
            val requestToken = sessionManager.getRequestToken()

            if (requestToken != null) completeLoginUseCase.execute()
        } else {
            val userId = userManager.getUserId()

            if (userId == null) {
                val isAccountDetailsUseCaseSuccessful = fetchAccountDetailsUseCase.execute()

                if (!isAccountDetailsUseCaseSuccessful) {
                    Timber.e("A problem with account details exists. User will be logged out.")

                    logoutUseCase.execute()
                }
            }
        }
    }
}