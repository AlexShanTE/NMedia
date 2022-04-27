package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.data.dto.Post
import ru.netology.nmedia.databinding.PostBinding
import java.math.RoundingMode
import java.text.DecimalFormat

internal class PostAdapter(
    private val onLikeCLicked: (Post) -> Unit,
    private val onShareClicked: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,onLikeCLicked,onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder(
        private val binding: PostBinding,
        onLikeCLicked: (Post) -> Unit,
        onShareClicked: (Post) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likesButton.setOnClickListener { onLikeCLicked(post) }
            binding.shareButton.setOnClickListener { onShareClicked(post) }
        }

        fun bind(post: Post) {

            this.post = post

            with(binding) {
                authorName.text = post.author
                date.text = post.published
                postTextContent.text = post.content
                numberOfLikes.text = numberFormatter(post.likes)
                numberOfShare.text = numberFormatter(post.shares)
                numberOfViews.text = numberFormatter(post.views)
                likesButton.setImageResource(getLikeIconResId(post.likedByMe))
            }
        }

        private fun numberFormatter(number: Long): String? {
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
    }

    private object DiffCallBack : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }
}