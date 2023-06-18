package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dao.ConstantValues.emptyPost
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

/** КЛАСС ДЛЯ РАБОТЫ С ПОСТАМИ, ОБРАБОТКИ ИЗМЕНЕНИЙ, ЛОВЛЯ ОШИБОК */

@HiltViewModel
class PostViewModel @Inject constructor (
    private val repository: PostRepository,
    appAuth: AppAuth,
) : ViewModel() {

    //состояние
    private val _state = MutableLiveData(FeedModelState())
    val state: LiveData<FeedModelState>
        get() = _state

    val data: LiveData<FeedModel> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            repository.data()
                .map { posts ->
                    FeedModel(
                        posts.map { post ->
                            post.copy(ownedByMe = post.authorId == myId)
                        },
                        posts.isEmpty()
                    )
                }
        }.asLiveData(Dispatchers.Default) //{ FeedModel(it, it.isEmpty())}

    private val edited = MutableLiveData(emptyPost)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _photo = MutableLiveData(
        PhotoModel(
            edited.value?.attachment?.url?.toUri(),
            edited.value?.attachment?.url?.toUri()?.toFile()
        )
    )
    val photo: LiveData<PhotoModel>
        get() = _photo

    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }

    init {
        loadPosts()
    }

    fun renameUrl(baseUrl: String, path: String, nameResource: String): String {
        return "$baseUrl/$path/$nameResource"
    }

    fun loadPosts() = viewModelScope.launch { //подключили карутину
        try {
            //обозначаем лоудинг
            _state.value = FeedModelState(loading = true)
            //отправка запроса
            repository.getAll()
            _state.value = FeedModelState()
        } catch (e: Exception) {
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
        } catch (e: Exception) {
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
//        TODO try {
//            _state.value = FeedModelState(loading = true)
//            repository.viewById(post)
//            _state.value = FeedModelState()
//        } catch (e: Exception) {
//            _state.value = FeedModelState(error = true)
//        }
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
        edited.value?.let { editedPost ->
            val newState = data.value?.posts.orEmpty()
                .map { if (it.id == editedPost.id) editedPost else it }
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

    //work with photo
    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }
}
