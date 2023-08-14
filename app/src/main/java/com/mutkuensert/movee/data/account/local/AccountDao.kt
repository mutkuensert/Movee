package com.mutkuensert.movee.data.account.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mutkuensert.movee.data.account.local.model.FavoriteMovieEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM FavoriteMovieEntity")
    suspend fun getFavoriteMovies(): List<FavoriteMovieEntity>

    @Query("SELECT * FROM FavoriteMovieEntity WHERE id = :id")
    suspend fun getFavoriteMovie(id: Int): FavoriteMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovies(vararg favoriteMovie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavoriteMovies(vararg favoriteMovie: FavoriteMovieEntity)

    @Query("DELETE FROM FavoriteMovieEntity")
    suspend fun clearAllFavoriteMovies()
}