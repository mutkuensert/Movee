package com.mutkuensert.movee.data.model.remote.search

import com.squareup.moshi.Json

interface MultiSearchResultMediaType {
    @Json(name = "media_type") val mediaType: String
}