package com.mutkuensert.movee.data.login.dto

import com.squareup.moshi.Json

data class SessionDto(
    val success: Boolean,
    @Json(name = "session_id") val sessionId: String
)
