package com.mutkuensert.movee.data.tvshow.model

import com.squareup.moshi.Json

data class PopularTvShowsResponse(
    val page: Int = 1,
    val results: List<PopularTvShowDto>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)

data class PopularTvShowDto(
    @Json(name = "poster_path") val posterPath: String?,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double,
    val name: String
)