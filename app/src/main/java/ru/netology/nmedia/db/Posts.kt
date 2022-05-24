package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.data.dto.Post

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    likes = getLong(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    shares = getLong(getColumnIndexOrThrow(PostsTable.Column.SHARES.columnName)),
    views = getLong(getColumnIndexOrThrow(PostsTable.Column.VIEWS.columnName)),
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
    videoContent = getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO_CONTENT.columnName))
)