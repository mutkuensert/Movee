package com.mutkuensert.movee.data.search

import com.mutkuensert.movee.data.search.model.MultiSearchModel
import com.mutkuensert.movee.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MultiSearchApi {

    @GET("search/multi")
    suspend fun multiSearch(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MultiSearchModel>
}