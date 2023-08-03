package com.mutkuensert.movee.domain.login

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.domain.account.AccountDetailsUseCase
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class CompleteLoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val sessionManager: SessionManager,
    private val accountDetailsUseCase: AccountDetailsUseCase,
) {

    suspend fun execute(): Boolean {
        val requestToken = sessionManager.getRequestToken() ?: return false

        authenticationRepository.fetchSessionIdWithValidatedRequestToken(requestToken)
            .onFailure {
                sessionManager.removeRequestToken()
                
                Timber.d(it.statusMessage)

                return false
            }
            .onSuccess { sessionId ->
                sessionManager.removeRequestToken()
                sessionManager.insertSessionId(sessionId)
            }

        accountDetailsUseCase.execute()

        return true
    }
}