package movee.data.authentication.model

import com.squareup.moshi.Json

data class SessionResponse(
    val success: Boolean,
    @Json(name = "session_id") val sessionId: String
)
