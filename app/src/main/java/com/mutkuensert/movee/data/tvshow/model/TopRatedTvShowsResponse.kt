package com.mutkuensert.movee.data.tvshow.model

import com.squareup.moshi.Json

data class TopRatedTvShowsResponse(
    val page: Int = 1,
    val results: List<TopRatedTvShowsResultDto>,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int
)

data class TopRatedTvShowsResultDto(
    @Json(name = "poster_path") val posterPath: String?,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double,
    val name: String
)