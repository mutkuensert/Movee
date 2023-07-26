package com.mutkuensert.movee.data.login

import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.onSuccess
import com.mutkuensert.movee.data.login.dto.LoginDto
import com.mutkuensert.movee.data.login.dto.SessionIdDto
import com.mutkuensert.movee.data.login.dto.ValidRequestTokenDto
import com.mutkuensert.movee.domain.login.LoginRepository
import com.mutkuensert.movee.library.authentication.SessionIdManager
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val sessionIdManager: SessionIdManager,
) : LoginRepository {
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

        sessionIdManager.insertSessionId(session.sessionId)

        return true
    }

    override suspend fun logout(): Boolean {
        val sessionId = sessionIdManager.getSessionId()

        if (sessionId != null) {
            authenticationApi.deleteSession(SessionIdDto(sessionId))
                .toResult()
                .onSuccess {
                    if (it.success) {
                        sessionIdManager.removeSessionId()
                        return true
                    }
                }
                .getOrElse { return false }
        }

        return false
    }
}