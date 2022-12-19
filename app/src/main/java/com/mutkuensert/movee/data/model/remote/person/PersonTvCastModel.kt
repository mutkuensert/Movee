package com.mutkuensert.movee.data.model.remote.person

import com.squareup.moshi.Json

data class PersonTvCastModel(
    val character: String,
    val name: String,
    val id: Int,
    @Json(name = "poster_path") val posterPath: String?
)
