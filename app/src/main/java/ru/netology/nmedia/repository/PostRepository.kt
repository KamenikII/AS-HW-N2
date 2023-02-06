package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

interface PostRepository {
    fun getAll(callback: Callback<List<Post>>)
    fun save(post: Post, callback: Callback<Unit>)
    fun edit(post: Post, callback: Callback<Unit>)
    fun removeById(id: Long, callback: Callback<Unit>)

    fun shareById(post: Post)
    fun viewById(post: Post)
    fun likeById(post: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(value: T) {}
        fun onError(e: Exception) {}
    }
}