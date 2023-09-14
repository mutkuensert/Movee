package com.mutkuensert.movee.data.tvshow.remote.model

import com.squareup.moshi.Json

data class TopRatedTvShowsResponse(
    val page: Int = 1,
    val results: List<TopRatedTvShowDto>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)

data class TopRatedTvShowDto(
    @Json(name = "poster_path") val posterPath: String?,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double,
    val name: String
)