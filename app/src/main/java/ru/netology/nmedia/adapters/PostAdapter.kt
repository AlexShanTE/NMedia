package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.data.dto.Post
import ru.netology.nmedia.databinding.PostBinding
import java.math.RoundingMode
import java.text.DecimalFormat

internal class PostAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: PostBinding,
        private val listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.postOptionsButton).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likesButton.setOnClickListener { listener.onLikeCLicked(post) }
            binding.shareButton.setOnClickListener { listener.onShareClicked(post) }
            binding.playButton.setOnClickListener { listener.onPlayVideoClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                date.text = post.published
                postTextContent.text = post.content
                likesButton.text = numberFormatter(post.likes)
                likesButton.isChecked = post.likedByMe
                shareButton.text = numberFormatter(post.shares)
                views.text = numberFormatter(post.views)
                postOptionsButton.setOnClickListener { popupMenu.show() }
                if (post.videoContent !== null) {
                    postVideoInfo.visibility = View.VISIBLE
                    videoDuration.setText("10:00")
                    videoTitle.setText("This should be VIDEO TITLE")
                }
            }
        }
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

private object DiffCallBack : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}


