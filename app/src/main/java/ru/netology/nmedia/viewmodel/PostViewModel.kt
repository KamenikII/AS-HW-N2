package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dataClasses.Like
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.dataClasses.Share
import ru.netology.nmedia.dataClasses.View
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryIml

private val postSample = Post (
    id = 0,
    content = "",
    author = "",
    published = "",
    likeCount = Like(),
    share = Share(),
    view = View()
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryIml()
    val data = repository.getAll()
    val edited = MutableLiveData(postSample)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun viewById(id: Long) = repository.viewById(id)
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = postSample
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun removeById(id: Long) = repository.removeById(id)
}