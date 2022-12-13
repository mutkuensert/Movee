package com.mutkuensert.movee.data.model.remote.movies.credits

data class MovieCredits(
    val id: Int,
    val cast: List<MovieCast>
)