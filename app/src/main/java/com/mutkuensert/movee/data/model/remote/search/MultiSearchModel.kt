package com.mutkuensert.movee.data.model.remote.search

import com.squareup.moshi.Json

data class MultiSearchModel(
    val page: Int,
    val results: List<MultiSearchResultMediaType>,
    @Json(name = "total_results") val totalResult: Int,
    @Json(name = "total_pages") val totalPages: Int
)
