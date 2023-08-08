package com.mutkuensert.movee.data.movie.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PopularMovieEntity(
    val id: Int,
    val page: Int,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    val title: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double
) {
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0
}
