package com.mutkuensert.movee.data.tvshow

import com.mutkuensert.movee.data.tvshow.model.PopularTvShowsModel
import com.mutkuensert.movee.data.tvshow.model.TopRatedTvShowsModel
import com.mutkuensert.movee.data.tvshow.model.TvDetailsModel
import com.mutkuensert.movee.data.tvshow.model.credits.TvShowCredits
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApi {

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("page") page: Int = 1
    ): Response<PopularTvShowsModel>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int = 1
    ): Response<TopRatedTvShowsModel>

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int
    ): Response<TvDetailsModel>

    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCredits(
        @Path("tv_id") tvId: Int
    ): Response<TvShowCredits>
}