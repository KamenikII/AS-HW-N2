package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun shareById(id: Long)
    fun viewById(id: Long)
    fun likeById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)
}