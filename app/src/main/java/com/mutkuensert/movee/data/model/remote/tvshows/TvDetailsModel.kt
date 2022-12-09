package com.mutkuensert.movee.data.model.remote.tvshows

import com.squareup.moshi.Json

data class TvDetailsModel(
    val id: Int,
    @Json(name = "original_name") val originalName: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    val seasons: List<SeasonModel>,
    @Json(name = "vote_average") val voteAverage: Double
)