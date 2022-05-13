package ru.netology.nmedia.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        binding.postContent.setText(args.initialContent)
        binding.postContent.requestFocus()
        binding.OK.setOnClickListener {
            onOkButtonClicked(binding)
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.postContent.text
        Log.d("TAG", text.toString() + "Post content fragment")
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
    }


}
