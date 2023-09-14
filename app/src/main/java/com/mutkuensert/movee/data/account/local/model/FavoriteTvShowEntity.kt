package com.mutkuensert.movee.data.account.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteTvShowEntity(
    @PrimaryKey val id: Int
)