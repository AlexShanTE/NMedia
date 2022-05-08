package ru.netology.nmedia.viewModel

import ru.netology.nmedia.util.SingleLiveEvent
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

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenToEditPost = SingleLiveEvent<String>()
    val navigateToPostContentScreenToAddNewPost = SingleLiveEvent<String>()
    val videoPlay = SingleLiveEvent<String?>()

    private val targetPost = MutableLiveData<Post?>(null)

    fun addNewPost(content: String) {
        if (content.isBlank()) return
        if (targetPost.value == null) {
            val newPost = Post(
                id = PostRepository.NEW_POST_ID,
                author = faker.name.name(),
                content = content,
                published = "Today",
                videoContent = "https://www.youtube.com/watch?v=QmPdnxWMVZk&ab_channel=SmileFun"
            )
            repository.add(newPost)
            targetPost.value = null
        }
    }

    fun editPost(content: String) {
        if (content.isBlank()) return
        val post = targetPost.value?.copy(content = content)
        if (post != null) {
            repository.edit(post)
        }
        targetPost.value = null
    }

    fun onAddClicked() {
        navigateToPostContentScreenToAddNewPost.call()
    }

    // region PostInteractionListener

    override fun onLikeCLicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        targetPost.value = post
        navigateToPostContentScreenToEditPost.value = post.content
    }

    override fun onPlayVideoClicked(post: Post) {
        if (post.videoContent == null) return
        else videoPlay.value = post.videoContent
    }

    // endregion PostInteractionListener

}