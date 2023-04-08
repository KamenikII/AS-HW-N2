package ru.netology.nmedia.model

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

/** МОДЕЛЬ ПОСТОВБ СПИСОКБ ОШИБКИ И ТП */

data class FeedModel(
    val posts: List<Post> = emptyList(), //список постов
    val loading: Boolean = false, //загрузка
    val error: Boolean = false, //ошибка
    val empty: Boolean = false, //пустота
    val refreshing: Boolean = false, //обновление
    val onSuccess: Boolean = false, //удача выполнения
    val onFailure: Boolean = false, //ошибка
    val connectionError: Boolean = false
)