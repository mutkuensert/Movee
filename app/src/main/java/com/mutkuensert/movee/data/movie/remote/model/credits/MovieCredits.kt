package com.mutkuensert.movee.data.movie.remote.model.credits

data class MovieCredits(
    val id: Int,
    val cast: List<MovieCast>
)