package ru.netology.nmedia.adapters

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
            like.setImageResource(
                if (post.likeByMe) R.drawable.ic_pressedlike else R.drawable.ic_unpressedlike
            )
            likeCount.text = post.likeCount.toString()

            //share
            shareItImage.setOnClickListener{
                onPostListener.onShare(post)
            }
            if (post.shareByMe) shareItImage.setImageResource(R.drawable.ic_shareitpressed)
            shareItCount.text = post.share.toString()

            //view
            viewCount.text = post.view.toString()
            onPostListener.view(post)
//            if (post.likedByMe) {
//                like.setImageResource(R.drawable.ic_liked_24)
//            }

        }
    }
}