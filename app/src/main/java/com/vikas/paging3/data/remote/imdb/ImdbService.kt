package com.vikas.paging3.data.remote.imdb

import com.vikas.paging3.data.remote.imdb.response.ImdbResponse
import com.vikas.paging3.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbService {

    @GET("Ratings/{api_key}/{movie_id}")
    suspend fun getImdbRating(
        @Path("api_key") api:String = Constants.IMDB_API_KEY,
        @Path("movie_id") movieId:String
    ): ImdbResponse
}