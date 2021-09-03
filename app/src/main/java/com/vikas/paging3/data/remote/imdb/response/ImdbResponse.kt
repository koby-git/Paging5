package com.vikas.paging3.data.remote.imdb.response

import com.google.gson.annotations.SerializedName

data class ImdbResponse(
    val imDbId : String,
    @SerializedName("imDb")
    val rating : String
)