package com.mutkuensert.movee.data.account.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int
)
