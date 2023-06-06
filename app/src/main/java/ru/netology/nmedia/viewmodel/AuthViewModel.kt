package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.dao.ConstantValues.noPhoto
import ru.netology.nmedia.dataClasses.MediaUpload
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.model.PhotoModel
import java.io.File
import java.io.IOException

/**КЛАСС, ОТВЕЧАЮЩИЙ ЗА ДЕЙСТВИЯ ПРИ АВТОРИЗАЦИИ*/

class AuthViewModel: ViewModel() {
    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }
    private val _dataState = MutableLiveData(-1)
    val dataState: LiveData<Int>
        get() = _dataState

    init {
        _dataState.value = -1
    }

    suspend fun login(login: String, pass: String) {
        viewModelScope.launch {
            val token: Token
            try {
                val response = PostsApi.retrofitService.login(login, pass)

                if (!response.isSuccessful) {
                    _dataState.value = 1

                    //throw ApiError(response.code(), response.message())
                } else {
                    token = response.body() ?: Token(id = 0, token = "")
                    AppAuth.getInstance().setAuth(token.id, token.token)
                    _dataState.value = 0
                }
            } catch (e: IOException) {
                _dataState.value = 2
                //throw NetworkError
            } catch (e: Exception) {
                _dataState.value = 3
                //throw UnknownError
            }
        }
    }

    suspend fun registerWithPhoto(login: String, pass: String, name: String, upload: MediaUpload?) {
        viewModelScope.launch {
            val token: Token
            try {
                val response = if (upload != null) {
                    PostsApi.retrofitService.registerWithPhoto(
                        login.toRequestBody("text/plain".toMediaType()),
                        pass.toRequestBody("text/plain".toMediaType()),
                        name.toRequestBody("text/plain".toMediaType()),
                        MultipartBody.Part.createFormData(
                            "file", upload.file.name, upload.file.asRequestBody()
                        )
                    )
                } else {
                    PostsApi.retrofitService.register(login,pass,name)
                }

                if (!response.isSuccessful) {
                    _dataState.value = 1

                    //throw ApiError(response.code(), response.message())
                } else {
                    token = response.body() ?: Token(id = 0, token = "")
                    AppAuth.getInstance().setAuth(token.id, token.token)
                    _dataState.value = 0
                }
            } catch (e: IOException) {
                _dataState.value = 2
                //throw NetworkError
            } catch (e: Exception) {
                _dataState.value = 3
                //throw UnknownError
            }
        }
    }
}