package com.example.app_readbook.model

import com.google.gson.annotations.SerializedName

data class ReadBook(
    @SerializedName("_id")
    val _id: String,
    var image: String?,
    var title: String?,
    var content: String?,
    var userID: User?,
    var status: String?,
    var like: List<LikePosts>?,
    var comment: List<CommentPosts>?,
    var notification: List<NotificationPost>?,
    var datepost: String?,
    var likeCount: Int?,
    var commentCount: Int?
)
data class NotificationPost(
    @SerializedName("_id")
    val _id: String,
    val user_id: User?,
    var statusSend: String?,
    var datetime: String?,
)
data class LikePosts(
    @SerializedName("_id")
    val _id: String,
    val user_id: User?,
    var status: String?,
    var like: List<LikePosts>
)

data class CommentPosts(
    @SerializedName("_id")
    val _id: String,
    val user_id: User,
    var content: String?,
    var image: String?,
    var comment: List<CommentPosts>
)
