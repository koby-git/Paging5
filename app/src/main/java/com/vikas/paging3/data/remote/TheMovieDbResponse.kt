package com.vikas.paging3.data.remote

import com.google.gson.annotations.SerializedName
import com.vikas.paging3.model.Movie

data class TheMovieDbResponse(
    var page : Int,
    var results : List<Movie>,
    @SerializedName("total_pages")
    var totalPage: Int,
    @SerializedName("total_results")
    var totalResults : Int
    )
