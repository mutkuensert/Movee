package com.mutkuensert.movee.data.movie.remote.model

import com.squareup.moshi.Json

data class MovieProductionCompany(
    val id: Int,
    @Json(name = "logo_path") val logoPath: String?,
    val name: String,
    @Json(name = "origin_country") val originCountry: String
)