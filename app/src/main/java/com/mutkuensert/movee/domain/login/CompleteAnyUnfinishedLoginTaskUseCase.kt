package com.mutkuensert.movee.domain.login

import com.mutkuensert.movee.domain.account.AccountDetailsUseCase
import com.mutkuensert.movee.library.session.SessionManager
import com.mutkuensert.movee.library.user.UserManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.last
import timber.log.Timber

@Singleton
class CompleteAnyUnfinishedLoginTaskUseCase @Inject constructor(
    private val accountDetailsUseCase: AccountDetailsUseCase,
    private val userManager: UserManager,
    private val sessionManager: SessionManager,
    private val logoutUseCase: LogoutUseCase,
    private val completeLoginUseCase: CompleteLoginUseCase,
) {

    suspend fun execute() {
        val isLoggedIn = sessionManager.isLoggedIn().last()

        if (!isLoggedIn) {
            val requestToken = sessionManager.getRequestToken()

            if (requestToken != null) completeLoginUseCase.execute()
        } else {
            val user = userManager.getCurrentUser()

            if (user == null) {
                val isAccountDetailsUseCaseSuccessful = accountDetailsUseCase.execute()

                if (!isAccountDetailsUseCaseSuccessful) {
                    Timber.e("A problem with account details exists. User will be logged out.")

                    logoutUseCase.execute()
                }
            }
        }
    }
}