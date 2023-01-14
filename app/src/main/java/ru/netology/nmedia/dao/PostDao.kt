package ru.netology.nmedia.dao

import ru.netology.nmedia.dataClasses.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun shareById(id: Long)
    fun viewById(id: Long)
    fun edit(post: Post)
}