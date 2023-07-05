package ru.netology.nmedia.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.dataClasses.FeedItem
import ru.netology.nmedia.dataClasses.Post

class PostDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Post, newItem: Post): Any =
        PayLoad(
            likeByMe = newItem.likeByMe.takeIf { oldItem.likeByMe != it },
            content = newItem.content.takeIf { oldItem.content != it },
        )
}