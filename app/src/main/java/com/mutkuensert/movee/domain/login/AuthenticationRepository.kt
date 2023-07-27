package com.mutkuensert.movee.domain.login

interface AuthenticationRepository {

    /**
     * @return true if the use successfully logged in, otherwise false.
     */
    suspend fun login(username: String, password: String): Boolean

    /**
     * @return true if the use successfully logged out, otherwise false
     */
    suspend fun logout(): Boolean
}