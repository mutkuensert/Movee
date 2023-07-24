package com.mutkuensert.movee.data.person.model

import com.squareup.moshi.Json

data class PersonMovieCastModel(
    val character: String,
    val title: String,
    val id: Int,
    @Json(name = "poster_path") val posterPath: String?
)
