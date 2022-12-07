package com.mutkuensert.movee.data.model.remote.tvshows

import com.squareup.moshi.Json

data class PopularTvShowsModel(
    val page: Int = 1,
    val results: List<PopularTvShowsResult>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)

data class PopularTvShowsResult(
    @Json(name = "poster_path") val posterPath: String?,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "original_name") val originalName: String
)