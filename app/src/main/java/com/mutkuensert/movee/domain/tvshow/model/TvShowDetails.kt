package com.mutkuensert.movee.domain.tvshow.model

data class TvShowDetails(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val seasonCount: Int,
    val totalEpisodeNumber: Int,
    val voteAverage: Double
)
