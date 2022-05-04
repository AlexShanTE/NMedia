package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.TextGenerator
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import kotlin.random.Random

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        List(100) {index ->
            Post(
                id = index +1L,
                author = "Alex",
                content = TextGenerator.generateRandomText(200),
                published = "25.04.2022",
                likes = Random.nextLong(0, 1200),
                shares = Random.nextLong(0, 1000),
                views = Random.nextLong(0, 100),
                likedByMe = false
            )
        }
    )

    private val posts
    get() = checkNotNull(data.value) {
        "data should be not null"
    }

    override fun like(postId:Long) {
        data.value = posts.map{
            if (it.id == postId) {
                when (it.likedByMe) {
                    true -> it.copy(likes = it.likes - 1, likedByMe = !it.likedByMe)
                    false -> it.copy(likes = it.likes + 1, likedByMe = !it.likedByMe)
                }
            } else it
        }
    }

    override fun share(postId:Long) {
        data.value = posts.map {
            if (it.id == postId) it.copy(shares = it.shares + 1)
            else it
        }
    }
}