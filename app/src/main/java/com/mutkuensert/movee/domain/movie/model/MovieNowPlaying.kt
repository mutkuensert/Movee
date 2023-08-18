package com.mutkuensert.movee.domain.movie.model

data class MovieNowPlaying(
    val imageUrl: String?,
    val title: String,
    val id: Int,
    val voteAverage: Double,
    val isFavorite: Boolean?
)
