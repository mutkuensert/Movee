package com.mutkuensert.movee.data.model.remote.tvshows

import com.squareup.moshi.Json

data class TopRatedTvShowsResult(
    @Json(name = "poster_path") val posterPath: String?,
    val id: Int,
    @Json(name = "vote_average") val voteAverage: Double,
    val name: String
)