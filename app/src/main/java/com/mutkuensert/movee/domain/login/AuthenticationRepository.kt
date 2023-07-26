package com.mutkuensert.movee.domain.login

import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun login(username: String, password: String): Boolean
    suspend fun logout(): Boolean
    suspend fun isLoggedIn(): Flow<Boolean>
}