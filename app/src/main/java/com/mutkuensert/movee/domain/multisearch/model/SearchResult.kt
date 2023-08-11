package com.mutkuensert.movee.domain.multisearch.model

data class SearchResult(
    val picturePath: String?,
    val title: String,
    val id: Int,
    val type: Type
) {
    enum class Type {
        MOVIE, TV, PERSON
    }
}
