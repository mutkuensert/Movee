package com.mutkuensert.movee.data.login.dto

import com.squareup.moshi.Json

data class AccountDetailsDto(
    val avatar: Avatar,
    val id: Int,
    @Json(name = "include_adult") val includeAdult: Boolean,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val username: String
)

data class Avatar(
    val gravatar: Gravatar,
    val tmdb: Tmdb
)

data class Gravatar(
    val hash: String?
)

data class Tmdb(
    @Json(name = "avatar_path") val avatarPath: String?
)