package ru.netology.nmedia.dataClasses

import androidx.room.Embedded

/** ДАТАКЛАСС, КОТОРЫЙ ОБОЗНАЧАЕТ СОДЕРЖАНИЯ (ЗНАЧЕНИЯ) ПОСТА */

data class Post(
    //info about post
    override val id: Long, //Уникальный индификатор поста
    val author: String = "Адмен XD", //Имя автора
    val authorImage: String? = null, //Аватарка автора
    val published: Long, //Дата публикации
    val authorId: Long, // ID автора
    val ownedByMe: Boolean = false, //Мой ли это пост

    //content
    val content: String, //Текст поста

    //link and files
    val urlOfVideo: String? = null, //Ссылка на видео
    @Embedded
    val attachment: Attachment? = null, //Приложенные файлы

    //statistic of post
    val likeByMe: Boolean = false, //поставлен ли пользователем лайк
    val likes: Int = 0,// Like = Like(), //кол-во лайков
    val shareByMe: Boolean = false, //делился ли пользователь постом
    val share: Int = 0,// Share = Share(), //кол-во репостов
    val viewItByMe: Boolean = false, //просмотрел ли пользователь пост
    val viewIt: Int = 0,
    val hidden: Boolean, //View = View(), //кол-во просмотров
) : FeedItem
