package ru.netology.nmedia.data.impl

import ru.netology.nmedia.db.PostDao
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel

class RoomRepository(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun like(postId: Long) {
       dao.likedById(postId)
    }

    override fun share(postId: Long) {
        dao.shareById(postId)
    }

    override fun delete(postId: Long) {
        dao.delete(postId)
    }

    override fun add(post: Post) {
       dao.add(post.toEntity())
    }

    override fun edit(post: Post) {
       dao.edit(post.toEntity())
    }

}