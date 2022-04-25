package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1,
            author = "Alex",
            content = "Hello, this is blablabla test text for my own post",
            published = "25.04.2022",
            likes = 1199,
            shares = 1590,
            views = 1000,
            likedByMe = false
        )

        binding.render(post)

        binding.postLayout.likesButton.setOnClickListener {
            println("hello im likeButton")
            val imageResId =
                if (post.likedByMe) {
                    post = post.copy(likes = post.likes - 1)
                    binding.render(post)
                    R.drawable.ic_like_24
                } else {
                    post = post.copy(likes = post.likes + 1)
                    binding.render(post)
                    R.drawable.ic_liked_24
                }
            post.likedByMe = !post.likedByMe
            binding.postLayout.likesButton.setImageResource(imageResId)
        }

        binding.postLayout.shareButton.setOnClickListener{
            post = post.copy(shares = post.shares + 1)
            binding.render(post)
        }

    }
}


private fun ActivityMainBinding.render(post: Post) {
    postLayout.authorName.text = post.author
    postLayout.date.text = post.published
    postLayout.postTextContent.text = post.content
    postLayout.numberOfLikes.text = numberFormatter(post.likes)
    postLayout.numberOfShare.text = numberFormatter(post.shares)
    postLayout.numberOfViews.text = numberFormatter(post.views)
}

fun numberFormatter(number: Long): String? {
    var value = number.toDouble()
    val arr = arrayOf("", "K", "M", "B")
    var index = 0
    while (value / 1000 >= 1) {
        value /= 1000
        index++
    }
    val decimalFormat = DecimalFormat("#.#")
    decimalFormat.roundingMode = RoundingMode.DOWN
    return java.lang.String.format("%s%s", decimalFormat.format(value), arr[index])
}