package ru.netology.nmedia.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    val id: Long = 0,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "published")
    val published: String,
    @ColumnInfo(name = "likes")
    val likes: Long ,
    @ColumnInfo(name = "shares")
    val shares: Long ,
    @ColumnInfo(name = "views")
    val views: Long ,
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean ,
    @ColumnInfo(name = "VideoContent")
    val videoContent: String?
)