package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import io.github.serpro69.kfaker.Faker
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import ru.netology.nmedia.db.PostDao

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {

    private val posts
        get() = checkNotNull(data.value) {
            "data should be not null"
        }

    override val data = MutableLiveData(dao.getAll())

    override fun like(postId: Long) {
        dao.likedById(postId)
        data.value = posts.map {
            if (it.id == postId) {
                when (it.likedByMe) {
                    true -> it.copy(likes = it.likes - 1, likedByMe = !it.likedByMe)
                    false -> it.copy(likes = it.likes + 1, likedByMe = !it.likedByMe)
                }
            } else it
        }
    }

    override fun share(postId: Long) {
        dao.shareById(postId)
        data.value = posts.map {
            if (it.id == postId) it.copy(shares = it.shares + 1)
            else it
        }
    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
        data.value = posts.filterNot {
            it.id == postId
        }
    }

    override fun add(post: Post) {
       val id = post.id
        val saved = dao.add(post)
        data.value = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id !== id) it else saved
            }
        }
    }

    override fun edit(post: Post) {
        dao.edit(post)
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    companion object {
        const val GENERATED_POST_AMOUNT = 2
    }

}