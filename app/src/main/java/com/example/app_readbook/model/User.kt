package com.example.app_readbook.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val _id: String,
    var image: String?,
    var username: String?,
    var password: String?,
)
