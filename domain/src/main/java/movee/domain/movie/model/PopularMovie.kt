package movee.domain.movie.model

/**
 *@param isFavorite is null if the user is not logged in.
 */
data class PopularMovie(
    val imageUrl: String?,
    val title: String,
    val id: Int,
    val isFavorite: Boolean?,
    val voteAverage: Double
)
