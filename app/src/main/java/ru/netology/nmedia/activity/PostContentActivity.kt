package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editPost.setText(intent.getStringExtra(RESULT_KEY_FOR_EDIT))
        binding.editPost.requestFocus()

        binding.OK.setOnClickListener {
            val intent = Intent()
            val text = binding.editPost.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(RESULT_KEY_FOR_ADD_NEW_POST, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<Unit, String?>() {
        override fun createIntent(context: Context, input: Unit): Intent =
            Intent(context, PostContentActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY_FOR_ADD_NEW_POST)
            } else {
                null
            }
        }
    }

    object ResultContractForEdit : ActivityResultContract<String, String?>() {
        override fun createIntent(context: Context, input: String): Intent {
            val intent = Intent(context, PostContentActivity::class.java)
            intent.putExtra(RESULT_KEY_FOR_EDIT, input)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY_FOR_ADD_NEW_POST)
            } else {
                null
            }
        }
    }

    private companion object {
        const val RESULT_KEY_FOR_ADD_NEW_POST = "postWithNewContent"
        const val RESULT_KEY_FOR_EDIT = "PostContentForEdit"
    }
}