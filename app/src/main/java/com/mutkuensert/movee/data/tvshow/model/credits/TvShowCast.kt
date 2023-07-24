package com.mutkuensert.movee.data.tvshow.model.credits

import com.squareup.moshi.Json

data class TvShowCast(
    val id: Int,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?,
    val character: String
)
