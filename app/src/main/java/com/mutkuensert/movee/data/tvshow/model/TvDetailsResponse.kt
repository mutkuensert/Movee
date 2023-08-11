package com.mutkuensert.movee.data.tvshow.model

import com.squareup.moshi.Json

data class TvDetailsResponse(
    val id: Int,
    val name: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    val seasons: List<SeasonDto>,
    @Json(name = "vote_average") val voteAverage: Double
)

data class SeasonDto(
    @Json(name = "episode_count") val episodeCount: Int
)