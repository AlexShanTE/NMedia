package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.adapters.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: PostViewModel by viewModels()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel)

        val postContentActivityLauncher =
            registerForActivityResult<Unit, String?>(PostContentActivity.ResultContract) { postContent ->
                postContent ?: return@registerForActivityResult
                viewModel.addNewPost(postContent)
                // Scroll to the top of the list
                GlobalScope.launch(Dispatchers.Main) {
                    delay(200)
                    val postList = binding.postsRecyclerView
                    if (postList.size > 1) {
                        binding.postsRecyclerView.scrollToPosition(0)
                    }
                }
            }

        val postContentActivityLauncherForEdit =
            registerForActivityResult<String, String?>(PostContentActivity.ResultContractForEdit) { postContent ->
                postContent ?: return@registerForActivityResult
                viewModel.editPost(postContent)
            }

        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.createPostButton.setOnClickListener {
            viewModel.onAddClicked()
        }

        viewModel.navigateToPostContentScreenToEditPost.observe(this) { postContent ->
            postContentActivityLauncherForEdit.launch(postContent)
        }

        viewModel.navigateToPostContentScreenToAddNewPost.observe(this) {
            postContentActivityLauncher.launch()
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
}










