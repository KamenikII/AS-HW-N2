package ru.netology.nmedia.adapters

import android.net.Uri
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.util.Numbers

class PostViewHolder(
    private val binding: FragmentCardPostBinding,
    private val onPostListener: OnPostListener //Описано в PostAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun renderingPostStructure(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.text = post.likeCount.toString()
            like.isChecked = post.likeByMe
            shareIt.text = post.share.toString()
            shareIt.isChecked = post.shareByMe
            view.text = post.view.toString()
            view.isChecked = post.viewItByMe
            if (!post.urlOfVideo.isNullOrBlank()) {
                videoLayout.visibility = View.VISIBLE
            } else {
                videoLayout.visibility = View.GONE
            }
            //like
            like.setOnClickListener{
                onPostListener.onLike(post)
            }
            like.isChecked = post.likeByMe
            like.text = post.likeCount.toString()

            //share
            shareIt.setOnClickListener{
                onPostListener.onShare(post)
            }

            shareIt.isChecked = post.shareByMe
            shareIt.text = post.share.toString()

            //view
            view.text = post.view.toString()
            onPostListener.view(post)

            //menu
            moreActions.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post) //при нажатие открывается менюшка res/menu/optional_post.xml
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onPostListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onPostListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            //Video by URL
            if (post.urlOfVideo != null) { //если есть ссылка на видео, отображать
                videoLayout.visibility = View.VISIBLE
            } else {
                videoLayout.visibility = View.GONE
            }

            //запуск видео
            videoLayout.setOnClickListener {
                onPostListener.onPlayVideo(post)
            }
        }
    }
//
//    fun bind(post: Post) {
//        //Работа с XML файлом(вёрстка, вид поста на экране, что выводим, как) res/layout/card_post.xml
//        //Реакции на кнопочки и прочее
//        binding.apply {
//            author.text = post.author
//            published.text = post.published
//            content.text = post.content
//
//            //like
//            like.setOnClickListener{
//                onPostListener.onLike(post)
//            }
//            like.isChecked = post.likeByMe
//            like.text = post.likeCount.toString()
//
//            //share
//            shareIt.setOnClickListener{
//                onPostListener.onShare(post)
//            }
//
//            shareIt.isChecked = post.shareByMe
//            shareIt.text = post.share.toString()
//
//            //view
//            view.text = post.view.toString()
//            onPostListener.view(post)
//
//            //menu
//            moreActions.setOnClickListener {
//                PopupMenu(it.context, it).apply {
//                    inflate(R.menu.options_post) //при нажатие открывается менюшка res/menu/optional_post.xml
//                    setOnMenuItemClickListener { item ->
//                        when (item.itemId) {
//                            R.id.remove -> {
//                                onPostListener.onRemove(post)
//                                true
//                            }
//                            R.id.edit -> {
//                                onPostListener.onEdit(post)
//                                true
//                            }
//
//                            else -> false
//                        }
//                    }
//                }.show()
//            }
//
//            //Video by URL
//            if (post.urlOfVideo != null) { //если есть ссылка на видео, отображать
//                videoLayout.visibility = View.VISIBLE
//            } else {
//                videoLayout.visibility = View.GONE
//            }
//
//            //запуск видео
//            videoLayout.setOnClickListener {
//                onPostListener.onPlayVideo(post)
//            }
//        }
//    }
}