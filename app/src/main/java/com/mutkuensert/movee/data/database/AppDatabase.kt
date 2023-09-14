package com.mutkuensert.movee.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mutkuensert.movee.data.account.local.AccountDao
import com.mutkuensert.movee.data.account.local.model.FavoriteMovieEntity
import com.mutkuensert.movee.data.account.local.model.FavoriteTvShowEntity
import com.mutkuensert.movee.data.movie.local.MovieDao
import com.mutkuensert.movee.data.movie.local.model.MovieNowPlayingEntity
import com.mutkuensert.movee.data.movie.local.model.PopularMovieEntity
import com.mutkuensert.movee.data.tvshow.local.TvShowDao
import com.mutkuensert.movee.data.tvshow.local.model.PopularTvShowEntity
import com.mutkuensert.movee.data.tvshow.local.model.TopRatedTvShowEntity

@Database(
    entities = [
        PopularMovieEntity::class,
        MovieNowPlayingEntity::class,
        PopularTvShowEntity::class,
        TopRatedTvShowEntity::class,
        FavoriteMovieEntity::class,
        FavoriteTvShowEntity::class,
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
}