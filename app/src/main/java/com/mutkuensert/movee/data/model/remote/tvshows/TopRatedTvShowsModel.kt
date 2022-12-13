package com.mutkuensert.movee.data.model.remote.tvshows

import com.squareup.moshi.Json

data class TopRatedTvShowsModel(
    val page: Int = 1,
    val results: List<TopRatedTvShowsResult>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)