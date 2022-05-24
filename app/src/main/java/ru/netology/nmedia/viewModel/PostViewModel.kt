package ru.netology.nmedia.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.util.SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.provider.App
import ru.netology.nmedia.adapters.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.dto.Post
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.impl.SQLiteRepository
import ru.netology.nmedia.data.impl.SharedPrefsPostRepository
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.ui.AppActivity
import ru.netology.nmedia.ui.FeedFragment
import ru.netology.nmedia.ui.FeedFragmentDirections

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = SQLiteRepository(
        dao = AppDb.getInstance(context = application).postDao
    )

    private val faker = Faker() // name generator

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreen = SingleLiveEvent<String>()
    val navigateToPostInfoScreen = SingleLiveEvent<Post>()
    val videoPlay = SingleLiveEvent<String?>()

     val targetPost = MutableLiveData<Post?>(null)

    fun addNewPost(content: String) {
        if (content.isBlank()) return
        Log.d("TAG",(targetPost.value == null).toString())
        if (targetPost.value == null) {
            val newPost = Post(
                id = PostRepository.NEW_POST_ID,
                author = faker.name.name(),
                content = content,
                published = "Today",
                videoContent = getRandomVideoContent()
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
        navigateToPostContentScreen.call()
    }

    private fun getRandomVideoContent(): String {
        val videoContentList = listOf<String>(
            "https://www.youtube.com/watch?v=QmPdnxWMVZk&t=2s&ab_channel=SmileFun",
            "https://www.youtube.com/watch?v=acAVHUxD1j0&ab_channel=FunnyAnimals",
            "https://www.youtube.com/watch?v=-452p_9ESbM&ab_channel=FANVIDOS-%D0%9C%D0%B8%D0%BB%D1%8B%D0%B5%D0%BA%D0%BE%D1%82%D0%B8%D0%BA%D0%B8",
            "https://www.youtube.com/watch?v=dhDi0CJN8FE&ab_channel=LifeforFun",
            "https://www.youtube.com/watch?v=3DrU3pFXSsY&ab_channel=SmileFun"
        )
        return videoContentList.random()
    }

     fun clearTargetPostValue(){
        targetPost.value = null
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
        navigateToPostContentScreen.value = post.content
    }

    override fun onPlayVideoClicked(post: Post) {
        if (post.videoContent == null) return
        else videoPlay.value = post.videoContent
    }

    override fun onPostItemCLicked(post: Post) {
        Toast.makeText(
            getApplication<Application>().applicationContext,
            "Clicked on post , author of post is ${post.author}",
            Toast.LENGTH_SHORT
        ).show()
        navigateToPostInfoScreen.value = post
    }

    // endregion PostInteractionListener

}