package movee.data.account.remote.model

import com.squareup.moshi.Json

data class AccountDetailsResponse(
    val avatar: AvatarDto,
    val id: Int,
    @Json(name = "include_adult") val includeAdult: Boolean,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val username: String
)

data class AvatarDto(
    val gravatar: GravatarDto,
    val tmdb: TmdbDto
)

data class GravatarDto(
    val hash: String?
)

data class TmdbDto(
    @Json(name = "avatar_path") val avatarPath: String?
)