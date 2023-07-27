package com.mutkuensert.movee.domain.login

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.library.user.UserDetails
import com.mutkuensert.movee.library.user.UserManager
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val accountRepository: AccountRepository,
    private val userManager: UserManager,
) {

    suspend fun execute(user: String, password: String): Boolean {
        val isLoggedIn = authenticationRepository.login(user, password)

        if (isLoggedIn) {
            accountRepository.getAccountDetails().onSuccess {
                userManager.setCurrentUser(
                    user = UserDetails(
                        avatarPath = it.avatar.tmdb.avatarPath,
                        gravatarHash = it.avatar.gravatar.hash,
                        id = it.id,
                        includeAdult = it.includeAdult,
                        iso_3166_1 = it.iso_3166_1,
                        iso_639_1 = it.iso_639_1,
                        name = it.name,
                        username = it.username
                    )
                )
            }
                .onFailure {
                    Timber.e(it.statusMessage)
                }

            return true
        }

        return false
    }
}