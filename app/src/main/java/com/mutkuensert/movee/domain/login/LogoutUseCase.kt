package com.mutkuensert.movee.domain.login

import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.library.user.UserManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userManager: UserManager,
    private val accountDao: AccountDao,
) {

    suspend fun execute(): Boolean {
        val isLoggedOut = authenticationRepository.logout()

        if (isLoggedOut) {
            userManager.removeCurrentUser()
            removeOtherUserRelatedInfo()

            return true
        }

        return false
    }

    private suspend fun removeOtherUserRelatedInfo() {
        accountDao.clearAllFavoriteMovies()
    }
}