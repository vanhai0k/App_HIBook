package com.example.app_readbook.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val _id: String,
    var image: String?,
    var username: String?,
    var password: String?,
    var friendsCount: Int?,
    var request: List<SendFriends>?,
    var friends: List<Friends>?
)
data class SendFriends(
    @SerializedName("_id")
    val _id: String?,
    val user_id: Any?,
    val status: String?
)
data class Friends(
    @SerializedName("_id")
    val _id:String?,
    val user_friend: Any?
)
