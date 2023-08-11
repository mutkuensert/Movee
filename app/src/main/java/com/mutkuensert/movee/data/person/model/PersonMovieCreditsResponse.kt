package com.mutkuensert.movee.data.person.model

import com.squareup.moshi.Json

data class PersonMovieCreditsResponse(
    val cast: List<PersonMovieCastDto>,
    val id: Int
)

data class PersonMovieCastDto(
    val character: String,
    val title: String,
    val id: Int,
    @Json(name = "poster_path") val posterPath: String?
)