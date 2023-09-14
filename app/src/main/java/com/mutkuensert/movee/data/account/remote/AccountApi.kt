package com.mutkuensert.movee.data.account.remote

import com.mutkuensert.movee.data.account.remote.model.FavoriteMovieDto
import com.mutkuensert.movee.data.account.remote.model.FavoriteMoviesResponse
import com.mutkuensert.movee.data.account.remote.model.FavoriteTvShowDto
import com.mutkuensert.movee.data.account.remote.model.FavoriteTvShowsResponse
import com.mutkuensert.movee.data.account.remote.model.PostFavoriteMovieResponse
import com.mutkuensert.movee.data.account.remote.model.PostFavoriteTvShowResponse
import com.mutkuensert.movee.data.authentication.model.AccountDetailsResponse
import com.mutkuensert.movee.network.PathParameters
import com.mutkuensert.movee.network.QueryParameters
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountApi {

    @GET("account")
    suspend fun getAccountDetails(@Query(QueryParameters.SESSION_ID) sessionId: String): Response<AccountDetailsResponse>

    @GET("account/${PathParameters.ACCOUNT_ID}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Query("page") page: Int = 1
    ): Response<FavoriteMoviesResponse>

    @POST("account/${PathParameters.ACCOUNT_ID}/favorite")
    suspend fun postFavoriteMovie(
        @Body favoriteMovieDto: FavoriteMovieDto
    ): Response<PostFavoriteMovieResponse>

    @GET("account/${PathParameters.ACCOUNT_ID}/favorite/tv")
    suspend fun getFavoriteTvShows(
        @Query("page") page: Int = 1
    ): Response<FavoriteTvShowsResponse>

    @POST("account/${PathParameters.ACCOUNT_ID}/favorite")
    suspend fun postFavoriteTvShow(
        @Body favoriteTvShowDto: FavoriteTvShowDto
    ): Response<PostFavoriteTvShowResponse>
}