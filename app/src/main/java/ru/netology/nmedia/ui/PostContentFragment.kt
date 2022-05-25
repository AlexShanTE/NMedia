package ru.netology.nmedia.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val prefs = context?.getSharedPreferences("repo", Context.MODE_PRIVATE)

        if (args.initialContent !== null) {
            binding.postContent.setText(args.initialContent)
        } else {
            val savedContent = prefs?.getString(SAVED_CONTENT, DEFAULT_VALUE_FOR_SAVED_CONTENT)
            binding.postContent.setText(savedContent)
            prefs?.edit { clear() }
        }
        binding.postContent.requestFocus()
        binding.OK.setOnClickListener {
            onOkButtonClicked(binding)
        }
        //Callback for backPressed
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val postContent = binding.postContent.text
            if (postContent.isNotBlank() && args.initialContent.isNullOrBlank()) {
                prefs?.edit {
                    putString(SAVED_CONTENT, postContent.toString())
                }
            }
            setFragmentResult(REQUEST_KEY, Bundle())
            findNavController().popBackStack()
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.postContent.text
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
        const val REQUEST_KEY = "requestKey"
        const val SAVED_CONTENT = "savedContent"
        const val DEFAULT_VALUE_FOR_SAVED_CONTENT = ""
    }
}
