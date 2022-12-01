package com.mutkuensert.movee.data.source

import com.mutkuensert.movee.data.PopularMovies
import com.mutkuensert.movee.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestService {

    @GET("/movie/popular")
    fun getPopularMovies(
      @Query("api_key") apiKey: String = API_KEY,
      @Query("page") page: Int
    ): Response<PopularMovies>
}