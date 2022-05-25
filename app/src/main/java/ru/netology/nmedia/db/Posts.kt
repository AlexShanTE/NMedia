package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.data.dto.Post

fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    likes = likes,
    shares = shares,
    views = views,
    likedByMe = likedByMe,
    videoContent = videoContent
)
fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    likes = likes,
    shares = shares,
    views = views,
    likedByMe = likedByMe,
    videoContent = videoContent
)