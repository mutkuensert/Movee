package com.mutkuensert.movee.data.search.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonResulItemModel(
    @Json(name = "profile_path") val profilePath: String?,
    val name: String,
    val id: Int
) : MultiSearchResultMediaType {

    @Json(name = "media_type")
    override val mediaType = MediaType.PERSON

}
