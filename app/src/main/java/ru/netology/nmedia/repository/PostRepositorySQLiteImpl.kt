package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import ru.netology.nmedia.dataClasses.Post
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.Exception

/** КЛАСС, ОТВЕЧАЮЩИЙ ЗА РЕАЛИЗАЦИЮ МЕТОДОВ РАБОТЫ С ПОСТОМ, РАБОТАЕТ С СЕРВЕРОМ И БД */

class PostRepositorySQLiteImpl : PostRepository {

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

    //получаем список постов
    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    when {
                        !response.isSuccessful -> callback.onError(Exception(response.message))
                        response.body == null -> callback.onError(Exception("Body is null"))
                        else -> callback.onSuccess((response.body?.string() ?: emptyList<Post>()) as List<Post>)
                    }

                    try {
                        val data = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(data, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    //сохраняем пост
    override fun save(post: Post, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    //удаление поста
    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }


    //Лайкаем пост
    override fun likeById(post:Post, callback: PostRepository.Callback<Post>) {
        val request: Request = if (post.likeByMe) {
            Request.Builder()
                .delete(gson.toJson(post).toRequestBody(jsonType))
                .url("${BASE_URL}/api/posts/${post.id}/likeCount")
                .build()
        } else {
            Request.Builder()
                .post(gson.toJson(post).toRequestBody(jsonType))
                .url("${BASE_URL}/api/slow/posts/${post.id}/likeCount")
                .build()
        }


        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    //поделиться постом
    override fun shareById(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/${post.id}/share")
            .build()
    }

    //просмотры поста пользователем
    override fun viewById(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/${post.id}/viewIt")
            .build()
    }

    //редактор поста
    override fun edit(post: Post, callback: PostRepository.Callback<Unit>) {
        save(post, callback)
    }
}