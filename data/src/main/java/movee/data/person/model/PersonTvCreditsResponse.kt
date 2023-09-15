package movee.data.person.model

import com.squareup.moshi.Json

data class PersonTvCreditsResponse(
    val cast: List<PersonTvCastDto>,
    val id: Int
)

data class PersonTvCastDto(
    val character: String,
    val name: String,
    val id: Int,
    @Json(name = "poster_path") val posterPath: String?
)