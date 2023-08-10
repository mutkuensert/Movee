package com.mutkuensert.movee.domain.login

import com.mutkuensert.movee.domain.ClearAccountRelatedCacheUseCase
import com.mutkuensert.movee.library.user.UserManager
import javax.inject.Inject
import javax.inject.Singleton

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