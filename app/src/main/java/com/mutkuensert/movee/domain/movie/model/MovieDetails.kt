package com.mutkuensert.movee.domain.movie.model

data class MovieDetails(
    val posterPath: String?,
    val title: String,
    val voteAverage: Double,
    val runtime: Int?,
    val overview: String?
)
