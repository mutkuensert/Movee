package com.mutkuensert.movee.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.account.local.entity.FavoriteMovieEntity
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.local.entity.PopularMovieEntity

@Database(entities = [PopularMovieEntity::class, FavoriteMovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun movieDao(): MovieDao
}