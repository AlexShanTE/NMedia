package ru.netology.nmedia.data.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Long = 0,
    val shares: Long = 0,
    val views: Long = 0,
    val likedByMe: Boolean = false,
    val videoContent: String? = null
) : Parcelable

