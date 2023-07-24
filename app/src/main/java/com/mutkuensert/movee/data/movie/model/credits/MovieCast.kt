package com.mutkuensert.movee.data.movie.model.credits

import com.squareup.moshi.Json

data class MovieCast(
    val id: Int,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?,
    val character: String
)
