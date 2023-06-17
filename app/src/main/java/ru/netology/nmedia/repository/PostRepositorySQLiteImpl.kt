package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.*
import ru.netology.nmedia.dataClasses.Post
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.errors.ApiError
import ru.netology.nmedia.errors.NetworkError
import java.io.IOException
import javax.inject.Inject

/** КЛАСС, ОТВЕЧАЮЩИЙ ЗА РЕАЛИЗАЦИЮ МЕТОДОВ РАБОТЫ С ПОСТОМ, РАБОТАЕТ С СЕРВЕРОМ И БД */

class PostRepositorySQLiteImpl @Inject constructor (
    private val postDao: PostDao,
    private val apiService: ApiService,
) : PostRepository {

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

    //override fun data() = postDao.getAll().map {it.map(PostEntity::toDto)}
    override fun data() = postDao
        .getAllVisible().map{it.map(PostEntity::toDto)}
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            //запрашиваем с сервера
            val response = apiService.getAll()

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
            val response = apiService.save(post)

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
            val response = apiService.removeById(id)

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
//TODO        try {
//            //запрашиваем с сервера
//            val response = apiService.shareById(post.id)
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
    }

    override suspend fun viewById(post: Post) {
        //postDao.viewItById(post.id)
/*TODO        try {
            //запрашиваем с сервера
            val response = apiService.viewItById(post.id)

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
                apiService.dislikeById(post.id)
            } else {
                apiService.likeById(post.id)

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

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        try {
            delay(120_000L)
            val response = apiService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            emit(body.size)
            postDao.insert(body.toEntity().map {
                it.copy(hidden = true)
            })
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}