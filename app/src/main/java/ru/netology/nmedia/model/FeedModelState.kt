package ru.netology.nmedia.model

/** Состояния поста / запросов */

data class FeedModelState(
    val error: Boolean = false, //ошибка
    var loading: Boolean = false, //пустота
    val refreshing: Boolean = false, //обновление

    val onSuccess: Boolean = false, //удача выполнения
    val onFailure: Boolean = false, //ошибка
    val connectionError: Boolean = false, //ошибка подключения
)