package com.mutkuensert.movee.data.person.model

import com.squareup.moshi.Json

data class PersonDetailsResponse(
    val id: Int,
    val name: String,
    val biography: String,
    @Json(name = "profile_path") val profilePath: String?
)
