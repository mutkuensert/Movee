package movee.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import movee.data.account.local.model.FavoriteMovieEntity
import movee.data.account.local.model.FavoriteTvShowEntity
import movee.data.movie.local.MovieDao
import movee.data.movie.local.model.MovieNowPlayingEntity
import movee.data.movie.local.model.PopularMovieEntity
import movee.data.tvshow.local.TvShowDao
import movee.data.tvshow.local.model.PopularTvShowEntity
import movee.data.tvshow.local.model.TopRatedTvShowEntity

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
    abstract fun accountDao(): movee.data.account.local.AccountDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
}