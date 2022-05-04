package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.data.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel
import java.math.RoundingMode
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel:PostViewModel by viewModels()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.postLayout.likesButton.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.postLayout.shareButton.setOnClickListener {
            viewModel.onShareClick()
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
    postLayout.likesButton.setImageResource(getLikeIconResId(post.likedByMe))
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

@DrawableRes
private fun getLikeIconResId(likedByMe: Boolean): Int {
    return if (likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
}