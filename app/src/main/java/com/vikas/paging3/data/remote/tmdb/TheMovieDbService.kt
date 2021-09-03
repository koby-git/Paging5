package com.vikas.paging3.data.remote.tmdb

import com.vikas.paging3.data.remote.tmdb.response.TheMovieDbResponse
import com.vikas.paging3.model.MovieVideoResponse
import com.vikas.paging3.util.Constants.INCLUDE_ADULTS
import com.vikas.paging3.util.Constants.INCLUDE_VIDEO
import com.vikas.paging3.util.Constants.SORT_BY_POPULARITY
import com.vikas.paging3.util.Constants.WITH_WATCH_MONETIZATION_TYPES
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbService {

  /*  @GET("/movie/{movieId}")
    suspend fun searchForMovie(
        @Path("movieId") movieId : Int ,
        @Query("api_key") api:String = BuildConfig.MOVIE_API_KEY
    ):Response<Movie>

*/

    @GET("movie/popular/")
    suspend fun getPopularMoviesList(
        @Query("page") page: Int
    ): TheMovieDbResponse

    @GET("discover/movie/")
    suspend fun getDiscoverMoviesList(
        @Query("sort_by") sortBy:String = SORT_BY_POPULARITY,
        @Query("include_adult") includeAdult:String = INCLUDE_ADULTS,
        @Query("include_video") includeVideo:String = INCLUDE_VIDEO,
        @Query("page") page:Int,
        @Query("with_watch_monetization_types") with_watch_monetization_types:String = WITH_WATCH_MONETIZATION_TYPES,
    ): TheMovieDbResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): TheMovieDbResponse

    @GET("search/movie")
    suspend fun getSearchedMovies(
        @Query("query") query: String,
        @Query("page") page: Int? = null,
        @Query("include_adult") includeAdult:String = INCLUDE_ADULTS,
    ): TheMovieDbResponse

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMovies(
        @Path("media_type") mediaType : String = MediaType.MOVIE,
        @Path("time_window") timeWindow : String = TimeWindow.WEEK,
    ): TheMovieDbResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovieListMovies(
        @Query("page") page: Int,
    ): TheMovieDbResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailsAndTrailers(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") videos: String = "videos,images",
    ): MovieVideoResponse

    @GET("trending/{media_type}/{time_window}")
    suspend fun getDailyTrendingMovies(
        @Path("media_type") mediaType : String = MediaType.MOVIE,
        @Path("time_window") timeWindow : String = TimeWindow.DAY,
    ): TheMovieDbResponse


    object MediaType{
            const val ALL = "all"
            const val MOVIE = "movie"
            const val TV = "tv"
            const val PERSON = "person"
    }

    object TimeWindow {
        const val DAY = "day"
        const val WEEK = "week"
    }

}