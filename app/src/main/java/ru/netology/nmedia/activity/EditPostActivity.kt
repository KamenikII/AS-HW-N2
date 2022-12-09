package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.myapplication.activity.POST_TEXT

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.editText) {
            requestFocus()
            val text = intent.getStringExtra(POST_TEXT)
            setText(text)
        }

        binding.saveEdit.setOnClickListener{
            val intent = Intent()

            if (binding.editText.text.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    this.getString(R.string.error_empty_content),
                    Toast.LENGTH_SHORT)
                    .show()
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.editText.text.toString()
                intent.putExtra(POST_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }

            finish()
        }

        binding.cancelEdit.setOnClickListener{
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }
}