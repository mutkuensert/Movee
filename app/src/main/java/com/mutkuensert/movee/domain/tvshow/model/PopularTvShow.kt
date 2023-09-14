package com.mutkuensert.movee.domain.tvshow.model

data class PopularTvShow(
    val imageUrl: String?,
    val id: Int,
    val voteAverage: Double,
    val name: String,
    val isFavorite: Boolean?,
)
