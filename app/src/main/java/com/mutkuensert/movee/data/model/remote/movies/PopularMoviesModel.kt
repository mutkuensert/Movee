package com.mutkuensert.movee.data.model.remote.movies

import com.squareup.moshi.Json

data class PopularMoviesModel(
    val page: Int,
    val results: List<PopularMoviesResult>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)