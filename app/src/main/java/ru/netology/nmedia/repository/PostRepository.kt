package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

/** ИНТЕРФЕЙС, В КОТОРОМ ОПИСАНЫ ВСЕ МЕТОДЫ РАБОТЫ С ПОСТОМ */

interface PostRepository {
    //служебное
    fun getAll(callback: Callback<List<Post>>) //возращает список фукнций
    fun save(post: Post, callback: Callback<Unit>) //сохраняет пост
    fun edit(post: Post, callback: Callback<Unit>) //изменяет пост
    fun removeById(id: Long, callback: Callback<Unit>) //удаляет пост

    //пользовательское
    fun shareById(post: Post) //поделиться постом
    fun viewById(post: Post) //просмотрено
    fun likeById(post: Post, callback: Callback<Post>) //лайк поста

    interface Callback<T> {
        fun onSuccess(value: T) {} //успех
        fun onError(e: Exception) {} //ошибка
    }
}