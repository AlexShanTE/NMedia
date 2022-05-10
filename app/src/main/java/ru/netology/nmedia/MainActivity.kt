package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapters.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyBoard
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: PostViewModel by viewModels()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.saveButton.setOnClickListener{
            with(binding.contentEditText){
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                clearFocus()
                hideKeyBoard()
            }
            binding.editTextGroup.visibility = View.GONE
        }

        binding.cancelEditTextButton.setOnClickListener{
            with(binding.contentEditText){
                clearFocus()
                text?.clear()
                hideKeyBoard()
            }
            binding.editTextGroup.visibility = View.GONE
        }

        viewModel.currentPost.observe(this) { currentPost ->
            if (currentPost?.content !== null) {
                binding.editTextGroup.visibility = View.VISIBLE
                binding.messageToEditText.text = currentPost.content
            }
            binding.contentEditText.setText(currentPost?.content)
        }
    }
}





