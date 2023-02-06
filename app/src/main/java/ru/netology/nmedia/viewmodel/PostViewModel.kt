package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.ConstantValues.emptyPost
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl(AppDb.getInstance(application).postDao())
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val edited = MutableLiveData(emptyPost)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun renameUrl(baseUrl: String, path: String, nameResource:String):String {
        return "$baseUrl/$path/$nameResource"
    }

    fun loadPosts() {

            _data.value = FeedModel(loading = true)

            repository.getAll(object : PostRepository.Callback<List<Post>>{
                override fun onSuccess(posts: List<Post>) {
                    _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
    }

    fun likeById(post: Post) {
        val post = data.value?.posts?.find { it.id == id } ?: emptyPost

        repository.likeById(post, object : PostRepository.Callback<Post> {
            override fun onSuccess(value: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) value.copy(authorAvatar =
                            if(!value.authorAvatar.isNullOrBlank()) {
                                renameUrl(PostRepositoryImpl.BASE_URL,"avatars",value.authorAvatar)
                            } else {
                                null
                            }, attachment =
                            if(value.attachment != null) {
                                value.attachment.copy(url = renameUrl(PostRepositoryImpl.BASE_URL,"images",value.attachment.url))
                            } else {
                                null
                            })
                            else it

                        }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(onFailure = true))
            }
        })
    }
    fun shareById(post: Post) = thread { repository.shareById(post) }
    fun viewById(post: Post) = thread { repository.viewById(post) }
    fun removeById(id: Long) {
        thread {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }
    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = emptyPost
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
}
