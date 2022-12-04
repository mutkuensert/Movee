package com.mutkuensert.movee.data

import com.squareup.moshi.Json

data class PopularMovies(
    val page: Int,
    val results: List<PopularMoviesResult>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)

data class PopularMoviesResult(
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "original_title") val originalTitle: String,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double
)
