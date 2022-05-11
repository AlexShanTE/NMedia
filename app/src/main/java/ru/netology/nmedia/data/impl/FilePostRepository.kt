package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.serpro69.kfaker.Faker
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import kotlin.properties.Delegates


class FilePostRepository(
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
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
            application.openFileOutput(FILE_NAME,Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
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
        const val NEXT_ID = "next id"
        const val FILE_NAME = "posts.json"
    }
}