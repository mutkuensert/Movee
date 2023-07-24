package com.mutkuensert.movee.data.tvshow.model

import com.squareup.moshi.Json

data class PopularTvShowsModel(
    val page: Int = 1,
    val results: List<PopularTvShowsResult>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)