package ru.netology.nmedia.adapters

import android.net.Uri
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.databinding.CardPostBinding

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onPostListener: OnPostListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

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
                    inflate(R.menu.options_post)
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
            if (post.urlOfVideo != null) {
                videoLayout.visibility = View.VISIBLE
//                videoView.apply {
//                    setVideoURI(Uri.parse(post.urlOfVideo))
//                    requestFocus()
//                    start()
//                }
            } else {
                videoLayout.visibility = View.GONE
            }

            videoLayout.setOnClickListener {
                onPostListener.onPlayVideo(post)
            }
        }
    }
}