package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val binding = ActivityMainBinding.inflate(layoutInflater)
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
        }
    }

    private fun subscribe() {
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
//              published.text = post.published
//              content.text = post.content
                like.setImageResource(
                    if (post.likeByMe) R.drawable.ic_pressedlike else R.drawable.ic_unpressedlike
                )
                likeCount.text = post.likeCount.toString()
                shareItImage.setImageResource(R.drawable.ic_shareitpressed)
                shareItCount.text = post.share.toString()
            }
        }
    }
//        val post = Post(
//            id = 1,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
//                    "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
//            published = "7 ноября 2022 в 12:30",
//            likeCount = Like(),
//            share = Share(),
//            view = View()
//        )
//
//        with(binding) {
//            author.text = post.author
//            published.text = post.published
//            content.text = post.content
//            if (!post.viewItByMe) {
//                post.view.viewAdd()
//                viewCount.text = post.view.toString()
//            }
//
//            like.setOnClickListener {
//                post.likeByMe = !post.likeByMe
//                if (post.likeByMe) {
//                    like.setImageResource(R.drawable.ic_pressedlike)
//                    post.likeCount.likeAdd()
//                } else {
//                    like.setImageResource(R.drawable.ic_unpressedlike)
//                    post.likeCount.likeDelete()
//                }
//                likeCount.text = post.likeCount.toString()
//            }
//
//            shareItImage.setOnClickListener {
//                post.shareByMe = true
//                shareItImage.setImageResource(R.drawable.ic_shareitpressed)
//                post.share.shareAdd()
//                shareItCount.text = post.share.toString()
//            }
//
//        }
//    }
}