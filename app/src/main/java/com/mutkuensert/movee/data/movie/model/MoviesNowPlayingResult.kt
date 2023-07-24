package com.mutkuensert.movee.data.movie.model

import com.squareup.moshi.Json

data class MoviesNowPlayingResult(
    @Json(name = "poster_path") val posterPath: String?,
    val title: String,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double
)