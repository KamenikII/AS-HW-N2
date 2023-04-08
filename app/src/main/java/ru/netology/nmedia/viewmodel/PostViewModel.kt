package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.Response.error
import ru.netology.nmedia.dao.ConstantValues.emptyPost
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread

/** КЛАСС ДЛЯ РАБОТЫ С ПОСТАМИ, ОБРАБОТКИ ИЗМЕНЕНИЙ, ЛОВЛЯ ОШИБОК */

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl(AppDb.getInstance(application).postDao())

    //состояние
    private val _state = MutableLiveData(FeedModelState())
    val state: LiveData<FeedModelState>
        get() = _state

    val data: LiveData<FeedModel> = repository.data().map { FeedModel(it, it.isEmpty())}

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

    fun loadPosts() = viewModelScope.launch { //подключили карутину
        try {
            //обозначаем лоудинг
            _state.value = FeedModelState(loading = true)
            //отправка запроса
            repository.getAll()
            _state.value = FeedModelState()
        } catch (e:Exception) {
            //выкидываем ошибку
            _state.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch { //подключили карутину
        try {
            //записываем лоудинг
            _state.value = FeedModelState(refreshing = true)
            //отправка запроса
            repository.getAll()
            _state.value = FeedModelState()
        } catch (e:Exception) {
            _state.value = FeedModelState(error = true)
        }
    }

    fun likeById(id: Long) = viewModelScope.launch {
        val post = data.value?.posts?.find { it.id == id } ?: emptyPost
        try {
            _state.value = FeedModelState(loading = true)
            repository.likeById(post)
            _state.value = FeedModelState()
        } catch (e: Exception) {
            _state.value = FeedModelState(error = true)
        }
    }

    fun shareById(post: Post) = viewModelScope.launch {
        try {
            _state.value = FeedModelState(loading = true)
            repository.shareById(post)
            _state.value = FeedModelState()
        } catch (e: Exception) {
            _state.value = FeedModelState(error = true)
        }
    }

    fun viewById(post: Post) = viewModelScope.launch {
        try {
            _state.value = FeedModelState(loading = true)
            repository.viewById(post)
            _state.value = FeedModelState()
        } catch (e: Exception) {
            _state.value = FeedModelState(error = true)
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            _state.value = FeedModelState(loading = true)
            repository.removeById(id)
            _state.value = FeedModelState()
        } catch (e: Exception) {
            _state.value = FeedModelState(error = true)
        }
    }

    fun save() {
        edited.value?.let {editedPost ->
            val newState = data.value?.posts.orEmpty()
                .map { if (it.id == editedPost.id) editedPost else it}
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    _state.value = FeedModelState(loading = true)
                    repository.save(editedPost)
                    _state.value = FeedModelState()
                } catch (e: Exception) {
                    _state.value = FeedModelState(error = true)
                }
            }
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) return
        edited.value = edited.value?.copy(content = text)
    }
}
