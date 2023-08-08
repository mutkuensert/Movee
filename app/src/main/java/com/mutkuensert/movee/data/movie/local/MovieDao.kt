package com.mutkuensert.movee.data.movie.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mutkuensert.movee.data.movie.local.entity.PopularMovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM PopularMovieEntity")
    fun getPopularMoviesPagingSource(): PagingSource<Int, PopularMovieEntity>

    @Query("SELECT * FROM PopularMovieEntity")
    fun getAllPopularMovies(): List<PopularMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg popularMovie: PopularMovieEntity)

    @Delete
    fun deletePopularMovieEntities(vararg popularMovie: PopularMovieEntity)

    @Query("DELETE FROM PopularMovieEntity")
    fun clearAllPopularMovies()
}