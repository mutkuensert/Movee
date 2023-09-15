package movee.data.movie.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import movee.data.movie.local.model.MovieNowPlayingEntity
import movee.data.movie.local.model.PopularMovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM PopularMovieEntity")
    fun getPopularMoviesPagingSource(): PagingSource<Int, PopularMovieEntity>

    @Query("SELECT * FROM PopularMovieEntity")
    fun getAllPopularMovies(): List<PopularMovieEntity>

    @Query("SELECT * FROM PopularMovieEntity WHERE id = :id")
    suspend fun getPopularMovie(id: Int): PopularMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPopularMovies(popularMovieList: List<PopularMovieEntity>)

    @Update
    fun update(popularMovie: PopularMovieEntity)

    @Delete
    fun deletePopularMovieEntities(vararg popularMovie: PopularMovieEntity)

    @Query("DELETE FROM PopularMovieEntity")
    fun clearAllPopularMovies()

    @Query("SELECT * FROM MovieNowPlayingEntity")
    fun getMoviesNowPlayingPagingSource(): PagingSource<Int, MovieNowPlayingEntity>

    @Query("SELECT * FROM MovieNowPlayingEntity")
    fun getAllMoviesNowPlaying(): List<MovieNowPlayingEntity>

    @Query("SELECT * FROM MovieNowPlayingEntity WHERE id = :id")
    suspend fun getMovieNowPlaying(id: Int): MovieNowPlayingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMoviesNowPlaying(movieNowPlayingEntity: List<MovieNowPlayingEntity>)

    @Update
    fun update(movieNowPlayingEntity: MovieNowPlayingEntity)

    @Delete
    fun deleteMovieNowPlayingEntities(vararg movieNowPlayingEntity: MovieNowPlayingEntity)

    @Query("DELETE FROM MovieNowPlayingEntity")
    fun clearAllMoviesNowPlaying()
}