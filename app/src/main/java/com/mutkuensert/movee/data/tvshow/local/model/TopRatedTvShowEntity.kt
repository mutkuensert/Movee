package com.mutkuensert.movee.data.tvshow.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopRatedTvShowEntity(
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
    ): TopRatedTvShowEntity {
        val copy = TopRatedTvShowEntity(
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