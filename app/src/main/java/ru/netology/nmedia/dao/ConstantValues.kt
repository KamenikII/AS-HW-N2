package ru.netology.nmedia.dao

import ru.netology.nmedia.dataClasses.Post

/** ОБЪЕКТ, ОТВЕЧАЮЩИЙ ЗА КОНСТАНТЫ */

object ConstantValues {
    const val POST_KEY = "POST_KEY"
    /* пример поста */
    val emptyPost = Post(
        id = 0,
        author = "Нетология",
        published = "now",
        content = "",
        likes = 0,
        share = 0,
        viewIt = 0,
        hidden = false
    )
}