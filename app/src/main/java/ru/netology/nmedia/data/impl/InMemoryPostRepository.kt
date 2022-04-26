package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        Post(
            id = 1,
            author = "Alex",
            content = "Hello, this is blablabla test text for my own post",
            published = "25.04.2022",
            likes = 1099,
            shares = 995,
            views = 100,
            likedByMe = false
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value)
        val likedPost =
            if (currentPost.likedByMe) {
                currentPost.copy(likes = currentPost.likes - 1, likedByMe = !currentPost.likedByMe)
            } else {
                currentPost.copy(likes = currentPost.likes + 1, likedByMe = !currentPost.likedByMe)
            }
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value)
        data.value = currentPost.copy(shares = currentPost.shares + 1)
    }
}