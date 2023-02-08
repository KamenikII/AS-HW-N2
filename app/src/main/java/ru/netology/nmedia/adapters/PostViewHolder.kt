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
            author.text = post.author //Автор поста
            published.text = post.published //Дата публикации
            content.text = post.content //текст поста
            like.text = post.likes.toString() //кол-во лайков
            like.isChecked = post.likeByMe //понравилось ли пользователю
            shareIt.text = post.share.toString() //кол-во репостов
            shareIt.isChecked = post.shareByMe //поделился ли пользователь
            view.text = post.viewIt.toString() //кол-во просмотров
            view.isChecked = post.viewItByMe //видел ли пользователь
            if (!post.urlOfVideo.isNullOrBlank()) { //если есть ссылка видео, то отобразить
                videoLayout.visibility = View.VISIBLE
            } else {
                videoLayout.visibility = View.GONE
            }
            //like listener
            like.setOnClickListener{
                onPostListener.onLike(post)
            }
            like.isChecked = post.likeByMe
            like.text = post.likes.toString()

            //share listener
            shareIt.setOnClickListener{
                onPostListener.onShare(post)
            }
            shareIt.isChecked = post.shareByMe
            shareIt.text = post.share.toString()

            //view listener
            view.text = post.viewIt.toString()
            onPostListener.view(post)

            //menu listener
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

            //запуск видео listener
            videoLayout.setOnClickListener {
                onPostListener.onPlayVideo(post)
            }
        }
    }

//    private fun postListener(post: Post) {
//        with(binding) {
//            //like listener
//            like.setOnClickListener{
//                like.isClickable = false //чтоб не было повтрного вопроса
//                like.text = post.likes.toString()
//                like.isChecked = post.likeByMe
//                onPostListener.onLike(post)
//            }
//
//            //share listener
//            shareIt.setOnClickListener{
//                shareIt.text = post.share.toString()
//                shareIt.isChecked = post.shareByMe
//                onPostListener.onShare(post)
//            }
//
//            //view listener
//            view.text = post.viewIt.toString()
//            onPostListener.view(post)
//
//            //menu listener
//            moreActions.setOnClickListener {
//                PopupMenu(it.context, it)
//                    .apply {
//                        inflate(R.menu.options_post) //при нажатие открывается менюшка res/menu/optional_post.xml
//                        setOnMenuItemClickListener { item ->
//                            when (item.itemId) {
//                                R.id.remove -> {
//                                    onPostListener.onRemove(post)
//                                    true
//                                }
//
//                                R.id.edit -> {
//                                    onPostListener.onEdit(post)
//                                    true
//                                }
//
//                                else -> false
//                            }
//                        }
//                }.show()
//            }
//
//            //video by URL listener
//            videoLayout.setOnClickListener {
//                onPostListener.onPlayVideo(post)
//            }
//
////            cardContent.setOnClickListener {
////                onPostListener.onPreviewPost(post)
////            }
//        }
//    }
}
