package ru.netology.nmedia.presentation

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepositoryInMemoryIml

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryIml()
    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
}