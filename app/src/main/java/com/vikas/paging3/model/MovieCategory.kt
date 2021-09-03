package com.vikas.paging3.model


data class MovieCategory(
    val title: String,
    val result: ArrayList<Movie> = ArrayList()
)