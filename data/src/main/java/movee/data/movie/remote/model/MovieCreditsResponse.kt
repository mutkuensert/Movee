package movee.data.movie.remote.model

import com.squareup.moshi.Json

data class MovieCreditsResponse(
    val id: Int,
    val cast: List<MovieCastDto>
)

data class MovieCastDto(
    val id: Int,
    val name: String,
    @Json(name = "profile_path") val profilePath: String?,
    val character: String
)