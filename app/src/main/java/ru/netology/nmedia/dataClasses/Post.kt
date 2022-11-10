package ru.netology.nmedia.dataClasses

data class Post (
    val id : Long,
    val author: String,
    val content: String,
    val published: String,

    val likeByMe: Boolean = false,
    val likeCount: Like,
    val shareByMe: Boolean = false,
    val share: Share,
    val viewItByMe: Boolean = false,
    val view: View
)
