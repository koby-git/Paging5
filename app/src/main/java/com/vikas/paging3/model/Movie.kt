package com.vikas.paging3.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.vikas.paging3.Constants.MOVIE_TABLE

@Entity(tableName = MOVIE_TABLE)
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String ? = null,
    @SerializedName("poster_path")
    var imageUrl:String? = null,
    @SerializedName("vote_average")
    val voteAverage: Double,

    //My Fields
    var isFavourite :Boolean = false,
    var isPopular:Boolean = false,
    var isDiscover:Boolean = false
)
