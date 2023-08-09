package com.mutkuensert.movee.domain.account

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.library.user.UserManager
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class AccountDetailsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userManager: UserManager
) {

    suspend fun execute(): Boolean {
        accountRepository.getUserDetails()
            .onSuccess {
                userManager.setCurrentUser(it)
                getOtherUserRelatedInfo()

                return true
            }
            .onFailure {
                Timber.e(it.statusMessage)
            }

        return false
    }

    private suspend fun getOtherUserRelatedInfo() {
        accountRepository.fetchAndInsertFavoriteMovies()
    }
}