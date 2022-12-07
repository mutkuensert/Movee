package com.mutkuensert.movee.data.api

import com.mutkuensert.movee.data.model.remote.movies.MovieDetailsModel
import com.mutkuensert.movee.data.model.remote.movies.MoviesNowPlayingModel
import com.mutkuensert.movee.data.model.remote.movies.PopularMoviesModel
import com.mutkuensert.movee.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

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

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieDetailsModel>
}