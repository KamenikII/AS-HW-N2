package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.*
import ru.netology.nmedia.dataClasses.Like
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.dataClasses.Share
import ru.netology.nmedia.dataClasses.View
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.presentation.PostViewModel

class MainActivity : AppCompatActivity() {
    lateinit var  binding: ActivityMainBinding
    val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribe()
        setListener()
    }

    private fun setListener() {
        with(binding) {
            like.setOnClickListener {
                viewModel.like()
            }
            shareItImage.setOnClickListener {
                viewModel.share()
            }
            viewModel.view()
        }
    }

    private fun subscribe() {
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
              published.text = post.published
              content.text = post.content
                //like
                like.setImageResource(
                    if (post.likeByMe) R.drawable.ic_pressedlike else R.drawable.ic_unpressedlike
                )
                likeCount.text = post.likeCount.toString()
                //share
                if (post.shareByMe) shareItImage.setImageResource(R.drawable.ic_shareitpressed)
                shareItCount.text = post.share.toString()
                //view
                viewCount.text = post.view.toString()
            }
        }
    }
}