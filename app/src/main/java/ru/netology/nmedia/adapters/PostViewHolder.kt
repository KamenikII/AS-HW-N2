package ru.netology.nmedia.adapters

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
            like.setIconResource(
                if (post.likeByMe) R.drawable.ic_pressedlike else R.drawable.ic_unpressedlike
            )
            like.text = post.likeCount.toString()

            //share
            shareIt.setOnClickListener{
                onPostListener.onShare(post)
            }
            if (post.shareByMe) shareIt.setIconResource(R.drawable.ic_shareitpressed)
            shareIt.text = post.share.toString()

            //view
            viewCount.text = post.view.toString()
            onPostListener.view(post)
//            if (post.likedByMe) {
//                like.setImageResource(R.drawable.ic_liked_24)
//            }

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
        }
    }
}