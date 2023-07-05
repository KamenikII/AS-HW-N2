package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.dataClasses.Ad
import ru.netology.nmedia.dataClasses.FeedItem
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.FragmentCardPostBinding

class PostsAdapter(
    private val onPostListener: OnPostListener
) : PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostDiffCallback()) {
    //определяем тип ViewHolder
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Ad -> R.layout.card_ad
            is Post -> R.layout.fragment_card_post
            null -> error("Unknown type of item")
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            R.layout.fragment_card_post -> {
                val binding = FragmentCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return PostViewHolder(binding, onPostListener)
            }
            R.layout.card_ad -> {
                val binding = CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return AdViewHolder(binding)
            }
            else -> error("Unknown type of item: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.renderingPostStructure(item)
            null -> error("Unknown type of item")
        }
    }

}

