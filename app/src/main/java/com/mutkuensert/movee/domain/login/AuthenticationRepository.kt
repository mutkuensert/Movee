package com.mutkuensert.movee.domain.login

import com.github.michaelbull.result.Result
import com.mutkuensert.movee.domain.Failure

interface AuthenticationRepository {
    suspend fun fetchRequestToken(): Result<String, Failure>
    suspend fun fetchSessionIdWithValidatedRequestToken(requestToken: String): Result<String, Failure>

    /**
     * @return true if the use successfully logged out, otherwise false
     */
    suspend fun logout(): Boolean
}