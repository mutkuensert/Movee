package com.mutkuensert.movee.data.api

import com.mutkuensert.movee.data.model.remote.tvshows.PopularTvShowsModel
import com.mutkuensert.movee.data.model.remote.tvshows.TopRatedTvShowsModel
import com.mutkuensert.movee.data.model.remote.tvshows.TvDetailsModel
import com.mutkuensert.movee.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApi {

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Response<PopularTvShowsModel>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Response<TopRatedTvShowsModel>

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<TvDetailsModel>
}