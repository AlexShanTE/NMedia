package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class LikeData (
    @SerializedName("userId")
    val userId : Long,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("postId")
    val postId: Long,
    @SerializedName("postAuthor")
    val postAuthor: String
)
