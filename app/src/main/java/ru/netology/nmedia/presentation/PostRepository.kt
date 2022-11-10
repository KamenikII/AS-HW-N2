package ru.netology.nmedia.presentation

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
    fun view()
}