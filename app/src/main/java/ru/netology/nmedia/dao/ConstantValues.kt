package ru.netology.nmedia.dao

import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.model.PhotoModel

/** ОБЪЕКТ, ОТВЕЧАЮЩИЙ ЗА КОНСТАНТЫ */

object ConstantValues {
    const val POST_KEY = "POST_KEY"
    /* пример поста */
    val emptyPost = Post(
        id = 0,
        author = "Нетология",
        published = 0,
        content = "",
        likes = 0,
        share = 0,
        viewIt = 0,
        hidden = false,
        authorId = 0L,
    )
    val noPhoto = PhotoModel()
}