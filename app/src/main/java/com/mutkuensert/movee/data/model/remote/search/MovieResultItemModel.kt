package com.mutkuensert.movee.data.model.remote.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResultItemModel(
    @Json(name = "poster_path") val posterPath: String?,
    val title: String,
    val id: Int
): MultiSearchResultMediaType{

    @Json(name = "media_type") override val mediaType = MediaType.movie

}
