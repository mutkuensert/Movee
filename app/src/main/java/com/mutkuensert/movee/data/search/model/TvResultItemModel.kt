package com.mutkuensert.movee.data.search.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvResultItemModel(
    @Json(name = "poster_path") val posterPath: String?,
    val name: String,
    val id: Int
) : MultiSearchResultMediaType {

    @Json(name = "media_type")
    override val mediaType = MediaType.TV

}
