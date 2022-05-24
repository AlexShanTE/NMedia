package ru.netology.nmedia.db

import ru.netology.nmedia.data.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun add(post: Post): Post
    fun edit(post:Post):Post
    fun likedById(id: Long)
    fun shareById(id:Long)
    fun removeById(id: Long)
}