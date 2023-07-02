package ru.netology.nmedia.dataClasses

data class Ad (
        override val id: Long,
        val image: String,
) : FeedItem