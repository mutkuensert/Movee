package com.mutkuensert.movee.data.tvshow.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mutkuensert.movee.data.tvshow.local.model.PopularTvShowEntity
import com.mutkuensert.movee.data.tvshow.local.model.TopRatedTvShowEntity

@Dao
interface TvShowDao {

    @Query("SELECT * FROM PopularTvShowEntity")
    fun getPopularTvShowsPagingSource(): PagingSource<Int, PopularTvShowEntity>

    @Query("SELECT * FROM PopularTvShowEntity")
    fun getAllPopularTvShows(): List<PopularTvShowEntity>

    @Query("SELECT * FROM PopularTvShowEntity WHERE id = :id")
    suspend fun getPopularTvShow(id: Int): PopularTvShowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPopularTvShows(list: List<PopularTvShowEntity>)

    @Update
    fun updatePopularTvShow(tvShow: PopularTvShowEntity)

    @Delete
    fun deletePopularTvShowEntities(vararg tvShow: PopularTvShowEntity)

    @Query("DELETE FROM PopularTvShowEntity")
    fun clearAllPopularTvShows()

    @Query("SELECT * FROM TopRatedTvShowEntity")
    fun getTopRatedTvShowsPagingSource(): PagingSource<Int, TopRatedTvShowEntity>

    @Query("SELECT * FROM TopRatedTvShowEntity")
    fun getAllTopRatedTvShows(): List<TopRatedTvShowEntity>

    @Query("SELECT * FROM TopRatedTvShowEntity WHERE id = :id")
    suspend fun getTopRatedTvShow(id: Int): TopRatedTvShowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopRatedTvShows(list: List<TopRatedTvShowEntity>)

    @Update
    fun updateTopRatedTvShow(tvShow: TopRatedTvShowEntity)

    @Delete
    fun deleteTopRatedTvShowEntities(vararg entity: TopRatedTvShowEntity)

    @Query("DELETE FROM TopRatedTvShowEntity")
    fun clearAllTopRatedTvShows()
}