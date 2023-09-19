package movee.data.account.remote

import movee.data.account.remote.model.FavoriteMovieDto
import movee.data.account.remote.model.FavoriteMoviesResponse
import movee.data.account.remote.model.FavoriteTvShowDto
import movee.data.account.remote.model.FavoriteTvShowsResponse
import movee.data.account.remote.model.PostFavoriteMovieResponse
import movee.data.account.remote.model.PostFavoriteTvShowResponse
import movee.data.authentication.model.AccountDetailsResponse
import movee.data.network.PathParameters
import movee.data.network.QueryParameters
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountApi {

    @GET("account")
    suspend fun getAccountDetails(@Query(QueryParameters.SESSION_ID) sessionId: String)
            : Response<AccountDetailsResponse>

    @GET("account/${PathParameters.ACCOUNT_ID}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Query("page") page: Int = 1,
        @Query(QueryParameters.SESSION_ID) sessionId: String,
    ): Response<FavoriteMoviesResponse>

    @POST("account/${PathParameters.ACCOUNT_ID}/favorite")
    suspend fun postFavoriteMovie(
        @Body favoriteMovieDto: FavoriteMovieDto,
        @Query(QueryParameters.SESSION_ID) sessionId: String,
    ): Response<PostFavoriteMovieResponse>

    @GET("account/${PathParameters.ACCOUNT_ID}/favorite/tv")
    suspend fun getFavoriteTvShows(
        @Query("page") page: Int = 1,
        @Query(QueryParameters.SESSION_ID) sessionId: String,
    ): Response<FavoriteTvShowsResponse>

    @POST("account/${PathParameters.ACCOUNT_ID}/favorite")
    suspend fun postFavoriteTvShow(
        @Body favoriteTvShowDto: FavoriteTvShowDto,
        @Query(QueryParameters.SESSION_ID) sessionId: String,
    ): Response<PostFavoriteTvShowResponse>
}