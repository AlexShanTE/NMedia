package ru.netology.nmedia.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        if (args.initialContent !== null) {
            binding.postContent.setText(args.initialContent)
        } else if (savedContent !== null) {
            binding.postContent.setText(savedContent)
            savedContent = null
        }
        binding.postContent.requestFocus()
        binding.OK.setOnClickListener {
            onOkButtonClicked(binding)
        }

        //Callback for backPressed
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val postContent = binding.postContent.text
            if (postContent.isNotBlank() && args.initialContent.isNullOrBlank()) {
                savedContent = postContent.toString()
            }
            /* next 4 lines crutch fix of a bug when a post is not created during a chain of events
            * edit -> backPressed -> addPost
            */
            val resultBundle = Bundle(1).apply {
                putString(CLEAR_TARGET_POST_COMMAND,"Clear")
            }
            setFragmentResult(REQUEST_KEY, resultBundle)

            findNavController().popBackStack()
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.postContent.text
        Log.d("TAG", text.toString())
        Log.d("TAG", args.initialContent.toString() + " initialContent")
        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            if (args.initialContent.isNullOrBlank()) {
                resultBundle.putString(RESULT_KEY_FOR_ADD_POST, text.toString())
            } else resultBundle.putString(RESULT_KEY_FOR_EDIT_POST, text.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }

    companion object {
        const val RESULT_KEY_FOR_ADD_POST = "addPost"
        const val RESULT_KEY_FOR_EDIT_POST = "editPost"
        const val CLEAR_TARGET_POST_COMMAND = "clearTargetPost"
        const val REQUEST_KEY = "requestKey"
        var savedContent: String? = null
    }

}
