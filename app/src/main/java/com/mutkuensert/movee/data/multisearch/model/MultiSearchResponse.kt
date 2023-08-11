package com.mutkuensert.movee.data.multisearch.model

import com.squareup.moshi.Json

data class MultiSearchResponse(
    val page: Int,
    val results: List<MultiSearchResultDto>,
    @Json(name = "total_results") val totalResult: Int,
    @Json(name = "total_pages") val totalPages: Int
)

interface MultiSearchResultDto {
    @Json(name = "media_type")
    val mediaType: String
}