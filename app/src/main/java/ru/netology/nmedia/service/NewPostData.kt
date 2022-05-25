package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class NewPostData(
    @SerializedName("postId")
    val postId: Long,
    @SerializedName("postAuthorId")
    val postAuthorId: String,
    @SerializedName("postAuthor")
    val postAuthor: String,
    @SerializedName("content")
    val content: String
)
