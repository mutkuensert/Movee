package com.mutkuensert.movee.data.movie.remote.model

import com.squareup.moshi.Json

data class PopularMoviesResponseModel(
    val page: Int,
    val results: List<PopularMoviesResultResponseModel>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)