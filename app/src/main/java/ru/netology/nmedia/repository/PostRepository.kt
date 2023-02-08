package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun shareById(post: Post)
    fun viewById(post: Post)
    fun likeById(post: Post): Post
    fun save(post: Post)
    fun edit(post: Post)
    fun removeById(id: Long)
}