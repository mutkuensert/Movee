package com.mutkuensert.movee.data.multisearch.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResultItemDto(
    @Json(name = "poster_path") val posterPath: String?,
    val title: String,
    val id: Int
) : MultiSearchResultDto {

    @Json(name = "media_type")
    override val mediaType = MediaType.MOVIE
}
