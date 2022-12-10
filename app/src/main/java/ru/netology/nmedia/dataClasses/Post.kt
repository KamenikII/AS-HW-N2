package ru.netology.nmedia.dataClasses

data class Post (
    //info about post
    val id : Long,
    val author: String,
    val content: String,
    val published: String,

    //statistic of post
    val likeByMe: Boolean = false,
    val likeCount: Like,
    val shareByMe: Boolean = false,
    val share: Share,
    val viewItByMe: Boolean = false,
    val view: View,

    //link and files
    val urlOfVideo: String? = null
)
