package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.serpro69.kfaker.Faker
import ru.netology.nmedia.adapters.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()
    private val faker = Faker() // name generator


    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return
        val newPost = currentPost.value?.copy(content = content) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = faker.name.name(),
            content = content,
            published = "Today"
        )
        repository.save(newPost)
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeCLicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) = repository.share(post.id)

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    // endregion PostInteractionListener

}