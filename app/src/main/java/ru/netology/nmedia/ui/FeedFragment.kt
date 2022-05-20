package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapters.PostAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel


open class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(requestKey = PostContentFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey !== PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(PostContentFragment.RESULT_KEY_FOR_ADD_POST)
            val editPostContent = bundle.getString(PostContentFragment.RESULT_KEY_FOR_EDIT_POST)
            when {
                newPostContent !== null -> viewModel.addNewPost(newPostContent)
                editPostContent !== null -> viewModel.editPost(editPostContent)
                else -> return@setFragmentResultListener
            }
        }

        viewModel.navigateToPostContentScreen.observe(this) { initialContent ->
            val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }

        viewModel.navigateToPostInfoScreen.observe(this) { post ->
            val direction = FeedFragmentDirections.toPostInfoItemFragment(post.id)
            findNavController().navigate(direction)
        }

        viewModel.videoPlay.observe(this) { videoLink ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
            val chooser = Intent.createChooser(intent, getString(R.string.open_with))
            startActivity(chooser)
        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = PostAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.notifyDataSetChanged()
            adapter.submitList(posts)
        }

        binding.createPostButton.setOnClickListener {
            viewModel.onAddClicked()
        }
    }.root

}










