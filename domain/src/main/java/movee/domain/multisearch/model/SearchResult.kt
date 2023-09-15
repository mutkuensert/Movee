package movee.domain.multisearch.model

data class SearchResult(
    val imageUrl: String?,
    val title: String,
    val id: Int,
    val type: Type
) {
    enum class Type {
        MOVIE, TV, PERSON
    }
}
