package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.databinding.FragmentCardPostBinding


//typealias OnLikeListener = (post: Post) -> Unit
interface OnPostListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun view(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onPlayVideo(post: Post) {}
}

class PostsAdapter(
    private val onPostListener: OnPostListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = FragmentCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onPostListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.renderingPostStructure(post)
    }
}

