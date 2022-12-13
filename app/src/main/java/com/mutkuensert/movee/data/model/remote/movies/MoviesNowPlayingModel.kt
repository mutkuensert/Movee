package com.mutkuensert.movee.data.model.remote.movies

import com.squareup.moshi.Json

data class MoviesNowPlayingModel(
    val page: Int,
    val results: List<MoviesNowPlayingResult>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)