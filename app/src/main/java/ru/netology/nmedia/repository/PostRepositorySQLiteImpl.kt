package ru.netology.nmedia.repository

import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dataClasses.Post
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {

    //играем с сервером
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    //получаем список постов
    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let { gson.fromJson(it, typeToken.type) }
    }

    //сохраняем пост
    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    //удаление поста
    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }


    //Лайкаем пост
    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    //поделиться постом
    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    //просмотры поста пользователем
    override fun viewById(id: Long) {
        dao.viewItById(id)
    }

    //редактор поста
    override fun edit(post: Post) {
        save(post)
    }
}