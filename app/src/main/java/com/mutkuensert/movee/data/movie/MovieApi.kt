package com.mutkuensert.movee.data.movie

import com.mutkuensert.movee.data.movie.remote.model.MovieDetailsModel
import com.mutkuensert.movee.data.movie.remote.model.MoviesNowPlayingModel
import com.mutkuensert.movee.data.movie.remote.model.PopularMoviesResponseModel
import com.mutkuensert.movee.data.movie.remote.model.credits.MovieCredits
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): Response<PopularMoviesResponseModel>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @Query("page") page: Int = 1
    ): Response<MoviesNowPlayingModel>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailsModel>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): Response<MovieCredits>
}