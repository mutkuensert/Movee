package com.mutkuensert.movee.data.tvshow

import com.mutkuensert.movee.data.tvshow.remote.model.PopularTvShowsResponse
import com.mutkuensert.movee.data.tvshow.remote.model.TopRatedTvShowsResponse
import com.mutkuensert.movee.data.tvshow.remote.model.TvShowCreditsResponse
import com.mutkuensert.movee.data.tvshow.remote.model.TvShowDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApi {

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("page") page: Int = 1
    ): Response<PopularTvShowsResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int = 1
    ): Response<TopRatedTvShowsResponse>

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int
    ): Response<TvShowDetailsResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCredits(
        @Path("tv_id") tvId: Int
    ): Response<TvShowCreditsResponse>
}