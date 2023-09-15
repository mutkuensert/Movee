package movee.data.movie

import movee.data.movie.remote.model.MovieCreditsResponse
import movee.data.movie.remote.model.MovieDetailsResponse
import movee.data.movie.remote.model.MoviesNowPlayingResponse
import movee.data.movie.remote.model.PopularMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): Response<PopularMoviesResponse>

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @Query("page") page: Int = 1
    ): Response<MoviesNowPlayingResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailsResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): Response<MovieCreditsResponse>
}