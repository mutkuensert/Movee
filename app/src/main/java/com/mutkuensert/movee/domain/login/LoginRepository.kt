package com.mutkuensert.movee.domain.login

interface LoginRepository {
    /**
     * @return TRUE if login is successful, otherwise FALSE
     */
    suspend fun login(username: String, password: String): Boolean

    /**
     * @return TRUE if logout is successful, otherwise FALSE
     */
    suspend fun logout(): Boolean
}