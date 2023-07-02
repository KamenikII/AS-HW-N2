package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dao.ConstantValues.emptyPost
import ru.netology.nmedia.dataClasses.FeedItem
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

/** КЛАСС ДЛЯ РАБОТЫ С ПОСТАМИ, ОБРАБОТКИ ИЗМЕНЕНИЙ, ЛОВЛЯ ОШИБОК */

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    appAuth: AppAuth,
) : ViewModel() {
    private val cached = repository
        .data
        .cachedIn(viewModelScope)

    //состояние
    private val _state = MutableLiveData(FeedModelState())
    val state: LiveData<FeedModelState>
        get() = _state

    val data: Flow<PagingData<FeedItem>> = appAuth.authStateFlow
        .flatMapLatest { (myId, _) ->
            cached.map { pagingData ->
                pagingData.map { post ->
                    if (post is Post) {
                        post.copy(ownedByMe = post.authorId == myId)
                    } else {
                        post
                    }
                }
            }
        }.flowOn(Dispatchers.Default)

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

    val newerCount = repository.getNewerCount()
        .catch { e -> e.printStackTrace() }

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
        //val post = data.value?.posts?.find { it.id == id } ?: emptyPost
        try {
            _state.value = FeedModelState(loading = true)
            repository.likeById(id)
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
