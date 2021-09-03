package com.vikas.paging3.data.local
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vikas.paging3.model.Movie


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>): LongArray

    @Query("DELETE FROM movie_table WHERE isDiscover = 1")
    suspend fun deleteDiscoverMovies()

    @Query("DELETE FROM movie_table WHERE isSearched = 1")
    suspend fun deleteSearchMovies()

    @Query("SELECT * FROM movie_table WHERE isDiscover = 1")
    fun getDiscoverMovieList() : PagingSource<Int,Movie>

    @Query("SELECT * FROM movie_table WHERE isSearched = 1")
    fun getSearchedMovieList() : PagingSource<Int,Movie>

    @Query("SELECT * FROM movie_table WHERE isPopular = 1")
    fun getPopularMovieList() : PagingSource<Int,Movie>

}
