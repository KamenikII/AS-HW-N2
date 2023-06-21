package ru.netology.nmedia.model

import ru.netology.nmedia.dataClasses.Post

/** МОДЕЛЬ ПОСТОВ, СПИСОК, ОШИБКИ И ТП. не dragger hilt*/

data class FeedModel(
    val posts: List<Post> = emptyList(), //список постов
    val empty: Boolean = false, //загрузка
)