package ru.netology.nmedia.dao

import ru.netology.nmedia.dataClasses.Post

/** ОБЪЕКТ, ОТВЕЧАЮЩИЙ ЗА КОНСТАНТЫ */

object ConstantValues {
    const val POST_KEY = "POST_KEY"
    /* пример поста */
    val emptyPost = Post(
        id = 0,
        content = "",
        author = "Нетология",
        likes = 0,
        share = 0,
        viewIt = 0,
        published = "now"
    )
}