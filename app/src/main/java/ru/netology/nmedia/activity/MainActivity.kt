package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                putExtra(Intent.EXTRA_TEXT, Uri.parse(videoLink))
            }
            startActivity(intent)
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










