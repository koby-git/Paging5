package com.vikas.paging3.data.remote

import com.vikas.paging3.Constants.API_KEY
import com.vikas.paging3.Constants.INCLUDE_ADULTS
import com.vikas.paging3.Constants.INCLUDE_VIDEO
import com.vikas.paging3.Constants.LANGUAGE
import com.vikas.paging3.Constants.SORT_BY_POPULARITY
import com.vikas.paging3.Constants.WITH_WATCH_MONETIZATION_TYPES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbService {

  /*  @GET("/movie/{movieId}")
    suspend fun searchForMovie(
        @Path("movieId") movieId : Int ,
        @Query("api_key") api:String = BuildConfig.MOVIE_API_KEY
    ):Response<Movie>

    @GET("movie/popular/")
    suspend fun getPopularMoviesList(
        @Query("api_key") api:String = BuildConfig.MOVIE_API_KEY
    ):Response<TheMovieDbResponse>
*/
    @GET("discover/movie/")
    suspend fun getDiscoverMoviesList(
        @Query("api_key") api:String = API_KEY,
        @Query("language") language:String = LANGUAGE,
        @Query("sort_by") sortBy:String = SORT_BY_POPULARITY,
        @Query("include_adult") includeAdult:String = INCLUDE_ADULTS,
        @Query("include_video") includeVideo:String = INCLUDE_VIDEO,
        @Query("page") page:Int,
        @Query("with_watch_monetization_types") with_watch_monetization_types:String = WITH_WATCH_MONETIZATION_TYPES,
    ):TheMovieDbResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):TheMovieDbResponse

}