package ru.netology.nmedia

data class Post (
    val id : Long,
    val author: String,
    val content: String,
    val published: String,
    var likeByMe: Boolean = false,
    var likeCount: Like,
    var shareByMe: Boolean = false,
    var share: Share,
    var viewItByMe: Boolean = false,
    var view: Long = 0
)
