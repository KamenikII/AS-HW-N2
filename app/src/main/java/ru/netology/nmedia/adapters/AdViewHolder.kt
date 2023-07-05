package ru.netology.nmedia.adapters

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dataClasses.Ad
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.view.load

class AdViewHolder (
    private val binding: CardAdBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(ad: Ad) {
        binding.adImage.load("${BuildConfig.BASE_URL}/media/${ad.image}")
    }
}