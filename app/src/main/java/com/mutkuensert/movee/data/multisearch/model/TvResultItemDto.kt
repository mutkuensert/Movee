package com.mutkuensert.movee.data.multisearch.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvResultItemDto(
    @Json(name = "poster_path") val posterPath: String?,
    val name: String,
    val id: Int
) : MultiSearchResultDto {

    @Json(name = "media_type")
    override val mediaType = MediaType.TV
}
