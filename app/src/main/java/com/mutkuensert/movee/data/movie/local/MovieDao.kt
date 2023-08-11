package com.mutkuensert.movee.data.movie.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mutkuensert.movee.data.movie.local.model.PopularMovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM PopularMovieEntity")
    fun getPopularMoviesPagingSource(): PagingSource<Int, PopularMovieEntity>

    @Query("SELECT * FROM PopularMovieEntity")
    fun getAllPopularMovies(): List<PopularMovieEntity>

    @Query("SELECT * FROM PopularMovieEntity WHERE id = :id")
    suspend fun getPopularMovie(id: Int): PopularMovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg popularMovie: PopularMovieEntity)

    @Update
    fun update(popularMovie: PopularMovieEntity)

    @Delete
    fun deletePopularMovieEntities(vararg popularMovie: PopularMovieEntity)

    @Query("DELETE FROM PopularMovieEntity")
    fun clearAllPopularMovies()
}