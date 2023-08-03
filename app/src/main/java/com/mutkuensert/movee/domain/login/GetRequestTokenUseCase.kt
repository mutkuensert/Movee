package com.mutkuensert.movee.domain.login

import com.github.michaelbull.result.getOrElse
import com.mutkuensert.movee.library.session.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRequestTokenUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val sessionManager: SessionManager,
) {

    suspend fun execute(): String? {
        val requestToken = authenticationRepository.fetchRequestToken().getOrElse { return null }
        sessionManager.insertRequestToken(requestToken)

        return requestToken
    }
}