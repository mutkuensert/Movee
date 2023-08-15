package com.mutkuensert.movee.data.tvshow.model

import com.squareup.moshi.Json

data class TvShowCreditsResponse(
    val id: Int,
    val cast: List<TvShowCastDto>
)

data class TvShowCastDto(
    val id: Int,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?,
    val character: String
)