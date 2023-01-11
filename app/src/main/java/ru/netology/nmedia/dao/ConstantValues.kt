package ru.netology.nmedia.dao

import ru.netology.nmedia.dataClasses.Post

object ConstantValues {
    const val POST_KEY = "POST_KEY"
    val emptyPost = Post(
        id = 0,
        content = "",
        author = "Нетология",
        likeCount = 0,
        share = 0,
        viewIt = 0,
        published = ""
    )
}