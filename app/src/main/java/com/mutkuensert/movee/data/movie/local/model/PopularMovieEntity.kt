package com.mutkuensert.movee.data.movie.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *@param isFavorite is null if the user is not logged in.
 */
@Entity
data class PopularMovieEntity(
    val id: Int,
    val page: Int,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    val title: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean?,
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0

    fun copyWithPrimaryKey(
        id: Int = this.id,
        page: Int = this.page,
        posterPath: String? = this.posterPath,
        title: String = this.title,
        voteAverage: Double = this.voteAverage,
        isFavorite: Boolean? = this.isFavorite
    ): PopularMovieEntity {
        val copy = PopularMovieEntity(
            id = id,
            page = page,
            posterPath = posterPath,
            title = title,
            voteAverage = voteAverage,
            isFavorite = isFavorite
        )

        copy.primaryKey = primaryKey

        return copy
    }
}
