package com.mutkuensert.movee.data

import com.squareup.moshi.Json

data class PopularMovies(
    val page: Int,
    val results: List<PopularMoviesResult>
)

data class PopularMoviesResult(
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "vote_average") val voteAverage: Double
)
