package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
                    "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
            published = "7 ноября 2022 в 12:30",
            likeCount = Like(),
            share = Share(),
            view = 0
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if (!post.viewItByMe) {
                post.view++
                viewCount.text = post.view.toString()
            }
            
            like.setOnClickListener {
                post.likeByMe = !post.likeByMe
                if (post.likeByMe) {
                    like.setImageResource(R.drawable.ic_pressedlike)
                    post.likeCount.likeAdd()
                } else {
                    like.setImageResource(R.drawable.ic_unpressedlike)
                    post.likeCount.likeDelete()
                }
                likeCount.text = post.likeCount.toString()
            }

            shareItImage.setOnClickListener {
                post.shareByMe = true
                shareItImage.setImageResource(R.drawable.ic_shareitpressed)
                post.share.shareAdd()
                shareItCount.text = post.share.toString()
            }
        }
    }
}