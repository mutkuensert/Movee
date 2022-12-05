package com.mutkuensert.movee.data.source

import com.mutkuensert.movee.data.MoviesNowPlayingModel
import com.mutkuensert.movee.data.PopularMoviesModel
import com.mutkuensert.movee.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
      @Query("api_key") apiKey: String = API_KEY,
      @Query("page") page: Int = 1
    ): Response<PopularMoviesModel>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Response<MoviesNowPlayingModel>
}