package com.example.app_readbook.model

import com.google.gson.annotations.SerializedName

data class MessageModel(
    @SerializedName("_id")
    val _id:String?,
    val receiver: User?,
    val sender: User?,
    val messages: List<MessageFriend>
)
data class MessageFriend(
    @SerializedName("_id")
    val _id:String,
    val user_id: User,
    val message: String,
    val timestamp: String
)
