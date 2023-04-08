package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import ru.netology.nmedia.dataClasses.Post
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.PostEntity
import java.io.IOException
import java.lang.Exception

/** КЛАСС, ОТВЕЧАЮЩИЙ ЗА РЕАЛИЗАЦИЮ МЕТОДОВ РАБОТЫ С ПОСТОМ, РАБОТАЕТ С СЕРВЕРОМ И БД */

class PostRepositorySQLiteImpl(private val postDao: PostDao) : PostRepository {

    //играем с сервером
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun data() = postDao.getAll().map {it.map(PostEntity::toDto)}

    override suspend fun getAll() {
        //запрашиваем с сервера
        val response = PostsApi.retrofitService.getAll()

        //ловим ошибки
        if (!response.isSuccessful) throw RuntimeException("Api error")
        response.body() ?: throw RuntimeException("Body is null")

        //записываем данные с сервера
        postDao.insert(response.body()!!.map {PostEntity.fromDto(it)})
    }

    override suspend fun save(post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun edit(post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun shareById(post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun viewById(post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun likeById(post: Post) {
        TODO("Not yet implemented")
    }
}