package ru.netology.nmedia.repository

import androidx.paging.PagingData
import ru.netology.nmedia.dataClasses.Post
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dataClasses.FeedItem

/** ИНТЕРФЕЙС, В КОТОРОМ ОПИСАНЫ ВСЕ МЕТОДЫ РАБОТЫ С ПОСТОМ */

interface PostRepository {
    //служебное
    val data: Flow<PagingData<FeedItem>> //
    suspend fun getAll() //возращает список фукнций
    suspend fun save(post: Post) //сохраняет пост
    suspend fun edit(post: Post) //изменяет пост
    suspend fun removeById(id: Long) //удаляет пост
    fun getNewerCount(): Flow<Int>

    //пользовательское
    suspend fun shareById(post: Post) //поделиться постом
    suspend fun viewById(post: Post) //просмотрено
    suspend fun likeById(id: Long) //лайк поста
}