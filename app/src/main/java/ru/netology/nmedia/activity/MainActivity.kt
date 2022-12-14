package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapters.OnPostListener
import ru.netology.nmedia.adapters.PostsAdapter
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.myapplication.activity.EditPostResultContract
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //функция вызываемая по завершении редактирования
    private val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
        result ?: return@registerForActivityResult
        viewModel.changeContent(result)
        viewModel.save()
    }

    //функция вызываемая по завершении создания поста
    private val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
        result ?: return@registerForActivityResult
        viewModel.changeContent(result)
        viewModel.save()
    }

    //адаптер
    private val adapter = PostsAdapter(object : OnPostListener {
        override fun onLike(post: Post) {
            viewModel.likeById(post.id)
        }

        override fun onShare(post: Post) {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(intent, "Share post")
            startActivity(shareIntent)
            viewModel.shareById(post.id)
        }

        override fun view(post: Post) {
            viewModel.viewById(post.id)
        }

        override fun onEdit(post: Post) {
            viewModel.edit(post)
            editPostLauncher.launch(post.content)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
        }

        override fun onPlayVideo(post: Post) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.urlOfVideo))
            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
//            with(binding.saveTextField) {
//                requestFocus()
//                setText(post.content)
//            }
        }

    }
}
