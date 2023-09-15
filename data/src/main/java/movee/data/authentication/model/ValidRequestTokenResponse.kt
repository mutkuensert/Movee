package movee.data.authentication.model

import com.squareup.moshi.Json

data class ValidRequestTokenResponse(
    @Json(name = "request_token") val validRequestToken: String
)
