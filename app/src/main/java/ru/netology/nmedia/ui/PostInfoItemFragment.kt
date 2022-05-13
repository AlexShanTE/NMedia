package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.handleCoroutineException
import ru.netology.nmedia.R
import ru.netology.nmedia.adapters.getIdFromYouTubeVideoLink
import ru.netology.nmedia.adapters.numberFormatter
import ru.netology.nmedia.databinding.PostInfoItemFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel


class PostInfoItemFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()

    private val args by navArgs<PostInfoItemFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Callback for backPressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.feedFragment)
        }

        val binding = PostInfoItemFragmentBinding.inflate(layoutInflater, container, false)

        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey !== PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val postContent = bundle.getString(PostContentFragment.RESULT_KEY_FOR_EDIT_POST)
            if (postContent != null) {
                viewModel.editPost(postContent)
            }
            binding.postItem.postTextContent.text = postContent
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == args.postId } ?: return@observe
            with(binding) {
                // region text values and icons setters
                postItem.authorName.text = post.author
                postItem.date.text = post.published
                postItem.postTextContent.text = post.content
                postItem.likesButton.isChecked = post.likedByMe
                postItem.likesButton.text = numberFormatter(post.likes).toString()
                postItem.shareButton.text = numberFormatter(post.shares).toString()
                postItem.views.text = numberFormatter(post.views).toString()
                // endregion text values and icons setters

                // region videoContent

                if (post.videoContent !== null) {
                    postItem.postVideoInfo.visibility = View.VISIBLE
                    postItem.videoDuration.text = "10:00"
                    postItem.videoTitle.text = "im too lay to get it form youtube api"
                    postItem.playButton.setOnClickListener {
                        viewModel.onPlayVideoClicked(post)
                    }
                    val videoId = getIdFromYouTubeVideoLink(post.videoContent)
                    /*
                    Yes, i know that's stupid to download this image one more time
                     */
                    Glide.with(postItem.videoPreview)
                        .asDrawable()
                        .centerCrop()
                        .load("https://img.youtube.com/vi/$videoId/mqdefault.jpg")
                        .error(R.mipmap.ic_launcher)
                        .into(postItem.videoPreview)
                }

                // endregion videoContent

                // region post options
                postItem.postOptionsButton.setOnClickListener {
                    PopupMenu(binding.root.context, postItem.postOptionsButton).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener {
                            when (it.itemId) {
                                R.id.remove -> {
                                    viewModel.onRemoveClicked(post)
                                    val direction = PostInfoItemFragmentDirections.toFeedFragment()
                                    findNavController().navigate(direction)
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.onEditClicked(post)
                                    val direction = PostInfoItemFragmentDirections.toPostContentFragment(post.content)
                                    findNavController().navigate(direction)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
                // endregion post options

                // region buttons clickListeners and button observers

                postItem.likesButton.setOnClickListener {
                    viewModel.onLikeCLicked(post)
                }

                postItem.shareButton.setOnClickListener {
                    viewModel.onShareClicked(post)
                }

                postItem.playButton.setOnClickListener{
                    viewModel.onPlayVideoClicked(post)
                }

                viewModel.videoPlay.observe(viewLifecycleOwner) { videoLink ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
                    val chooser = Intent.createChooser(intent, getString(R.string.open_with))
                    startActivity(chooser)
                }

                viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, postContent)
                        type = "text/plain"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }

                // endregion buttons clickListeners and button observers
            }
        }
        return binding.root
    }
}
