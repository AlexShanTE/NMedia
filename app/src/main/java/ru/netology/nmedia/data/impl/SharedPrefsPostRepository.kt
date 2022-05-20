package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import io.github.serpro69.kfaker.Faker
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import kotlin.properties.Delegates


class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences("repo", Context.MODE_PRIVATE)

    private var nextId by Delegates.observable(
        prefs.getLong(NEXT_ID, 0L)
    ) { _, _, newValue ->
        prefs.edit {
            putLong(NEXT_ID, newValue)
        }
    }

    private val faker = Faker()

    private var posts
        get() = checkNotNull(data.value) {
            "data should be not null"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POST_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val serializedPost = prefs.getString(POST_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPost !== null) {
            Json.decodeFromString(serializedPost)
        } else emptyList()
        data = MutableLiveData(posts)
    }


    override fun like(postId: Long) {
        posts = posts.map {
            if (it.id == postId) {
                when (it.likedByMe) {
                    true -> it.copy(likes = it.likes - 1, likedByMe = !it.likedByMe)
                    false -> it.copy(likes = it.likes + 1, likedByMe = !it.likedByMe)
                }
            } else it
        }
    }

    override fun share(postId: Long) {
        posts = posts.map {
            if (it.id == postId) it.copy(shares = it.shares + 1)
            else it
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filterNot {
            it.id == postId
        }
    }

    override fun add(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) {
            posts = listOf(post.copy(id = ++nextId)) + posts // to the start of list
//            posts = posts + post.copy(id = ++nextId) //to the end
        }
    }

    override fun edit(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    companion object {
        const val POST_PREFS_KEY = "post prefs key"
        const val NEXT_ID = "next id"
    }
}