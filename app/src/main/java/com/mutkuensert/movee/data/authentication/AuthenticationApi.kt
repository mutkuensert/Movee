package com.mutkuensert.movee.data.authentication

import com.mutkuensert.movee.data.authentication.dto.DeleteSessionDto
import com.mutkuensert.movee.data.authentication.dto.LoginDto
import com.mutkuensert.movee.data.authentication.dto.RequestTokenDto
import com.mutkuensert.movee.data.authentication.dto.SessionDto
import com.mutkuensert.movee.data.authentication.dto.SessionIdDto
import com.mutkuensert.movee.data.authentication.dto.ValidRequestTokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
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

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(@Body sessionIdDto: SessionIdDto): Response<DeleteSessionDto>
}