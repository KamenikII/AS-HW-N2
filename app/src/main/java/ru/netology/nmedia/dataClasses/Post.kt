package ru.netology.nmedia.dataClasses

data class Post (
    //info about post
    val id : Long,
    val author: String = "Netology",
    val content: String,
    val published: String,

    //statistic of post
    val likeByMe: Boolean = false,
    val likeCount: Int = 0,
    val shareByMe: Boolean = false,
    val share: Int = 0,
    val viewItByMe: Boolean = false,
    val viewIt: Int = 0,

    //link and files
    val urlOfVideo: String? = null
)
