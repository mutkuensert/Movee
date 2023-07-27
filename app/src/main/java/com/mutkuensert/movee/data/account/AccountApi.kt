package com.mutkuensert.movee.data.account

import com.mutkuensert.movee.data.account.dto.FavoriteMoviesDto
import com.mutkuensert.movee.data.authentication.dto.AccountDetailsDto
import com.mutkuensert.movee.network.PathParameters
import com.mutkuensert.movee.network.QueryParameters
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountApi {

    @GET("account")
    suspend fun getAccountDetails(@Query(QueryParameters.SESSION_ID) sessionId: String): Response<AccountDetailsDto>

    @GET("account/${PathParameters.ACCOUNT_ID}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Query("page") page: Int = 1
    ): Response<FavoriteMoviesDto>
}