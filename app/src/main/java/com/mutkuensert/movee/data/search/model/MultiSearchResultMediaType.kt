package com.mutkuensert.movee.data.search.model

import com.squareup.moshi.Json

interface MultiSearchResultMediaType {
    @Json(name = "media_type")
    val mediaType: String
}