package com.mutkuensert.movee.data.multisearch.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonResulItemDto(
    @Json(name = "profile_path") val profilePath: String?,
    val name: String,
    val id: Int
) : MultiSearchResultDto {

    @Json(name = "media_type")
    override val mediaType = MediaType.PERSON
}
