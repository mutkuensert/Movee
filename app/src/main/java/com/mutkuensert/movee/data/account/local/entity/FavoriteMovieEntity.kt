package com.mutkuensert.movee.data.account.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mutkuensert.movee.domain.account.model.FavoriteMovie

@Entity
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int
) {
    fun toFavoriteMovie(): FavoriteMovie {
        return FavoriteMovie(id = id)
    }
}
