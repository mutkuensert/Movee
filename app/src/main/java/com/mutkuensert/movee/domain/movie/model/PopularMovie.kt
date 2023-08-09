package com.mutkuensert.movee.domain.movie.model

data class PopularMovie(
    val posterPath: String?,
    val title: String,
    val id: Int,
    val isFavorite: Boolean?,
    val voteAverage: Double
)
