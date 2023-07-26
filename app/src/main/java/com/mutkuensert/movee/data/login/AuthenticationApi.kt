package com.mutkuensert.movee.data.login

import com.mutkuensert.movee.data.login.dto.AccountDetailsDto
import com.mutkuensert.movee.data.login.dto.DeleteSessionDto
import com.mutkuensert.movee.data.login.dto.LoginDto
import com.mutkuensert.movee.data.login.dto.RequestTokenDto
import com.mutkuensert.movee.data.login.dto.SessionDto
import com.mutkuensert.movee.data.login.dto.SessionIdDto
import com.mutkuensert.movee.data.login.dto.ValidRequestTokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationApi {

    @GET("authentication/token/new")
    suspend fun getRequestToken(): Response<RequestTokenDto>

    @POST("authentication/token/validate_with_login")
    suspend fun getValidatedRequestTokenWithLogin(@Body loginDto: LoginDto): Response<RequestTokenDto>

    @POST("authentication/session/new")
    suspend fun getSessionWithValidatedRequestToken(
        @Body validRequestTokenDto: ValidRequestTokenDto
    ): Response<SessionDto>

    @DELETE("authentication/session")
    suspend fun deleteSession(@Body sessionIdDto: SessionIdDto): Response<DeleteSessionDto>

    @GET("account")
    suspend fun getAccountDetails(): Response<AccountDetailsDto>
}