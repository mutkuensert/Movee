package com.mutkuensert.movee.data.authentication

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.data.authentication.model.SessionIdDto
import com.mutkuensert.movee.data.authentication.model.ValidRequestTokenResponse
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.login.AuthenticationError
import com.mutkuensert.movee.domain.login.AuthenticationRepository
import com.mutkuensert.movee.library.session.SessionManager
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject
import timber.log.Timber

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val sessionManager: SessionManager,
) : AuthenticationRepository {

    override suspend fun fetchRequestToken(): Result<String, Failure> {
        authenticationApi.getRequestToken()
            .toResult()
            .onSuccess {
                return if (!it.success) {
                    Err(Failure(statusMessage = "Unsuccessful request token"))
                } else {
                    Ok(it.requestToken)
                }
            }

        return Err(Failure(statusMessage = ""))
    }

    override suspend fun fetchSessionIdWithValidatedRequestToken(requestToken: String): Result<String, Failure> {
        authenticationApi.getSessionWithValidatedRequestToken(
            validRequestTokenResponse = ValidRequestTokenResponse(
                validRequestToken = requestToken
            )
        ).toResult()
            .onSuccess {
                return if (!it.success) {
                    Err(Failure(statusMessage = "Unsuccessful request token validation."))
                } else {
                    Ok(it.sessionId)
                }
            }

        return Err(Failure(statusMessage = ""))
    }

    override suspend fun logout(): Boolean {
        val sessionId = sessionManager.getSessionId()

        if (sessionId != null) {
            authenticationApi.deleteSession(SessionIdDto(sessionId))
                .toResult()
                .onSuccess {
                    if (it.success) {
                        sessionManager.removeSession()
                        Timber.d("User session has been removed.")
                        return true
                    }
                }
                .onFailure {
                    if (it.statusCode == AuthenticationError.RESOURCE_NOT_FOUND.statusCode) {
                        sessionManager.removeSession()
                        Timber.e("User session has been removed. Error Message: ${it.statusMessage}")
                        return true
                    }

                    return false
                }
        }

        return false
    }
}