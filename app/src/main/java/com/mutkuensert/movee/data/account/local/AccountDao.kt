package com.mutkuensert.movee.data.account.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mutkuensert.movee.data.account.local.model.FavoriteMovieEntity
import com.mutkuensert.movee.data.account.local.model.FavoriteTvShowEntity

@Dao
interface AccountDao {

    @Query("SELECT EXISTS(SELECT * FROM FavoriteMovieEntity WHERE id = :id)")
    suspend fun isMovieFavorite(id: Int): Boolean

    @Query("SELECT * FROM FavoriteMovieEntity WHERE id = :id")
    suspend fun getFavoriteMovie(id: Int): FavoriteMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovies(vararg favoriteMovie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavoriteMovies(vararg favoriteMovie: FavoriteMovieEntity)

    @Query("DELETE FROM FavoriteMovieEntity")
    suspend fun clearAllFavoriteMovies()

    @Query("SELECT EXISTS(SELECT * FROM FavoriteTvShowEntity WHERE id = :id)")
    suspend fun isTvShowFavorite(id: Int): Boolean

    @Query("SELECT * FROM FavoriteTvShowEntity WHERE id = :id")
    suspend fun getFavoriteTvShow(id: Int): FavoriteTvShowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTvShows(vararg favoriteTvShow: FavoriteTvShowEntity)

    @Delete
    suspend fun deleteFavoriteTvShows(vararg favoriteTvShow: FavoriteTvShowEntity)

    @Query("DELETE FROM FavoriteTvShowEntity")
    suspend fun clearAllFavoriteTvShows()
}