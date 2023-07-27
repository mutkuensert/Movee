package com.mutkuensert.movee.data.account.dto

data class FavoriteMoviesDto(
    val page: Int,
    val results: List<FavoriteMoviesResult>,
    val total_pages: Int,
    val total_results: Int
)