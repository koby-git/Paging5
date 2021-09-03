package com.vikas.paging3.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovieVideoResponse(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    val title: String? = null,
    @SerializedName("release_date")
    val releaseDate: String ? = null,
    @SerializedName("poster_path")
    var imageUrl:String? = null,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("overview")
    val description: String,
    var imdbRating:String? = null,
    @SerializedName("imdb_id")
    var imdbId:String,
    var genres : ArrayList<Genre>,

    val videos : VideoList
    ) : Serializable