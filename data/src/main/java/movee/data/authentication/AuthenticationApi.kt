package movee.data.authentication

import movee.data.authentication.model.DeleteSessionResponse
import movee.data.authentication.model.RequestTokenResponse
import movee.data.authentication.model.SessionIdDto
import movee.data.authentication.model.SessionResponse
import movee.data.authentication.model.ValidRequestTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthenticationApi {

    @GET("authentication/token/new")
    suspend fun getRequestToken(): Response<RequestTokenResponse>

    @POST("authentication/session/new")
    suspend fun getSessionWithValidatedRequestToken(
        @Body validRequestTokenResponse: ValidRequestTokenResponse
    ): Response<SessionResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(@Body sessionIdDto: SessionIdDto): Response<DeleteSessionResponse>
}