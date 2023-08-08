package com.mutkuensert.movee.data.movie.remote.model

import com.squareup.moshi.Json

data class PopularMoviesResultResponseModel(
    @Json(name = "poster_path") val posterPath: String?,
    val title: String,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double
)