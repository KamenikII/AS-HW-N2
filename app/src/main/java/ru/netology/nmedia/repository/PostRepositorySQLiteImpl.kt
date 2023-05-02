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
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.errors.ApiError
import ru.netology.nmedia.errors.NetworkError
import java.io.IOException

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
        try {
            //запрашиваем с сервера
            val response = PostsApi.retrofitService.getAll()

            //ловим ошибки
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            //записываем данные с сервера
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(body.toEntity())
        } catch (e: IOException) { //ловим ошибки
            throw NetworkError
        }
    }


    override suspend fun save(post: Post) {
        try {
            //запрашиваем с сервера
            val response = PostsApi.retrofitService.save(post)

            //ловим ошибки
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            //записываем данные с сервера
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.save(PostEntity.fromDto(body))
        } catch (e: IOException) { //ловим ошибки
            throw NetworkError
        }
    }

    override suspend fun edit(post: Post) {
        save(post)
    }

    override suspend fun removeById(id: Long) {
        try {
            //запрашиваем с сервера
            val response = PostsApi.retrofitService.removeById(id)

            //ловим ошибки
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            //записываем данные с сервера
            postDao.removeById(id)
        } catch (e: IOException) { //ловим ошибки
            throw NetworkError
        }
    }

    override suspend fun shareById(post: Post) {
        postDao.shareById(post.id)
/*        try {
//            //запрашиваем с сервера
//            val response = PostsApi.retrofitService.shareById(post.id)
//
//
//            //ловим ошибки
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//
//            //записываем данные с сервера
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            postDao.insert(PostEntity.fromDto(body))
//        } catch (e: IOException) { //ловим ошибки
//            throw NetworkError
//        }
*/    }

    override suspend fun viewById(post: Post) {
        postDao.viewItById(post.id)
/*        try {
            //запрашиваем с сервера
            val response = PostsApi.retrofitService.viewItById(post.id)

            //ловим ошибки
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            //записываем данные с сервера
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) { //ловим ошибки
            throw NetworkError
        }
*/    }

    override suspend fun likeById(post: Post) {
        postDao.likeById(post.id)
        try {
            //запрашиваем с сервера
            val response = if (post.likeByMe) {
                PostsApi.retrofitService.dislikeById(post.id)
            } else {
                PostsApi.retrofitService.likeById(post.id)

            }

            //ловим ошибки
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            //записываем данные с сервера
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) { //ловим ошибки
            throw NetworkError
        }
    }
}