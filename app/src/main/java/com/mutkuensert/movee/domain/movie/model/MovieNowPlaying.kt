package com.mutkuensert.movee.domain.movie.model

data class MovieNowPlaying(
    val posterPath: String?,
    val title: String,
    val id: Int,
    val voteAverage: Double
)
