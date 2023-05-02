package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

/** ИНТЕРФЕЙС, В КОТОРОМ ОПИСАНЫ ВСЕ МЕТОДЫ РАБОТЫ С ПОСТОМ */

interface PostRepository {
    //служебное
    fun data(): LiveData<List<Post>> //
    suspend fun getAll() //возращает список фукнций
    suspend fun save(post: Post) //сохраняет пост
    suspend fun edit(post: Post) //изменяет пост
    suspend fun removeById(id: Long) //удаляет пост

    //пользовательское
    suspend fun shareById(post: Post) //поделиться постом
    suspend fun viewById(post: Post) //просмотрено
    suspend fun likeById(post: Post) //лайк поста
}