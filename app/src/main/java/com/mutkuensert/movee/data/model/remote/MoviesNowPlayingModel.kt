package com.mutkuensert.movee.data.model.remote

import com.squareup.moshi.Json

data class MoviesNowPlayingModel(
    val page: Int,
    val results: List<MoviesNowPlayingResult>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)

data class MoviesNowPlayingResult(
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "original_title") val originalTitle: String,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double
)
