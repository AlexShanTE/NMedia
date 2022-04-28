package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import io.github.serpro69.kfaker.Faker
import ru.netology.nmedia.TextGenerator
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import kotlin.random.Random


class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POST_AMOUNT.toLong()
    private val faker = Faker()

    override val data = MutableLiveData(
        List(GENERATED_POST_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = faker.name.name(),
                content = TextGenerator.generateRandomText(20),
                published = "28.04.2022",
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

    override fun like(postId: Long) {
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
        data.value = posts.map {
            if (it.id == postId) it.copy(shares = it.shares + 1)
            else it
        }
    }

    override fun delete(postId: Long) {
        data.value = posts.filter {
            it.id !== postId
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        data.value = posts + post.copy(id = ++nextId) //to the end
//        data.value = listOf(post.copy(id = ++nextId)) + posts // to the start of list
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    companion object {
        const val GENERATED_POST_AMOUNT = 5
    }
}