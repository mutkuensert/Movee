package com.mutkuensert.movee.data.account.remote

import com.mutkuensert.movee.data.account.remote.model.FavoriteMovieRequestModel
import com.mutkuensert.movee.data.account.remote.model.FavoriteMovieResponseModel
import com.mutkuensert.movee.data.account.remote.model.FavoriteMoviesResponseModel
import com.mutkuensert.movee.data.authentication.dto.AccountDetailsDto
import com.mutkuensert.movee.network.PathParameters
import com.mutkuensert.movee.network.QueryParameters
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountApi {

    @GET("account")
    suspend fun getAccountDetails(@Query(QueryParameters.SESSION_ID) sessionId: String): Response<AccountDetailsDto>

    @GET("account/${PathParameters.ACCOUNT_ID}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Query("page") page: Int = 1
    ): Response<FavoriteMoviesResponseModel>

    @POST("account/${PathParameters.ACCOUNT_ID}/favorite")
    suspend fun postFavoriteMovie(
        @Body favoriteMovieRequestModel: FavoriteMovieRequestModel
    ): Response<FavoriteMovieResponseModel>
}