package com.mutkuensert.movee.data.authentication

import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.data.authentication.dto.LoginDto
import com.mutkuensert.movee.data.authentication.dto.SessionIdDto
import com.mutkuensert.movee.data.authentication.dto.ValidRequestTokenDto
import com.mutkuensert.movee.domain.login.AuthenticationError
import com.mutkuensert.movee.domain.login.AuthenticationRepository
import com.mutkuensert.movee.library.session.SessionManager
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val sessionManager: SessionManager,
) : AuthenticationRepository {

    override suspend fun login(username: String, password: String): Boolean {
        val requestToken = authenticationApi.getRequestToken()
            .toResult()
            .onSuccess { if (!it.success) return false }
            .getOrElse { return false }

        val validToken = authenticationApi.getValidatedRequestTokenWithLogin(
            loginDto = LoginDto(
                username = username,
                password = password,
                requestToken = requestToken.requestToken
            )
        ).toResult()
            .onSuccess { if (!it.success) return false }
            .getOrElse { return false }

        val session = authenticationApi.getSessionWithValidatedRequestToken(
            validRequestTokenDto = ValidRequestTokenDto(
                validRequestToken = validToken.requestToken
            )
        ).toResult()
            .onSuccess { if (!it.success) return false }
            .getOrElse { return false }

        sessionManager.insertSessionId(session.sessionId)

        return true
    }

    override suspend fun logout(): Boolean {
        val sessionId = sessionManager.getSessionId()

        if (sessionId != null) {
            authenticationApi.deleteSession(SessionIdDto(sessionId))
                .toResult()
                .onSuccess {
                    if (it.success) {
                        sessionManager.removeSession()
                        return true
                    }
                }
                .onFailure {
                    if (it.statusCode == AuthenticationError.RESOURCE_NOT_FOUND.statusCode) {
                        sessionManager.removeSession()
                        return true
                    }

                    return false
                }
        }

        return false
    }

    override suspend fun isLoggedIn(): Flow<Boolean> {
        return sessionManager.isLoggedIn()
    }
}