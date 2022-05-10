package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>

    fun like(postId: Long)

    fun share(postId: Long)

    fun delete(postId: Long)

    fun add(post: Post)

    fun edit(post: Post)

    companion object {
        const val NEW_POST_ID = 0L
    }

}