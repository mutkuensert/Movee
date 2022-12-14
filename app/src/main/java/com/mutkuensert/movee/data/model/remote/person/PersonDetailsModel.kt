package com.mutkuensert.movee.data.model.remote.person

import com.squareup.moshi.Json

data class PersonDetailsModel(
    val id: Int,
    val name: String,
    val biography: String,
    @Json(name = "profile_path") val profilePath: String?
)
