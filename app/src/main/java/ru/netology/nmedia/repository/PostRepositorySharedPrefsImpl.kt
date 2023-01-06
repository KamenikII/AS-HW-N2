package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dataClasses.Post

class PostRepositorySharedPrefsImpl(
    private val context: Context
) : PostRepository {
    private var nextId = 1L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    /*чем отличается PostRepositoryFileIml и PostRepositorySharedPrefsIml*/
    private val prefs = context.getSharedPreferences("repository", Context.MODE_PRIVATE)
    private val key = "posts"
    private val gsonFile = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "Posts.json"

    init { //инициализация gson файла из json
        prefs.getString(key, null)?.let {
            posts = gsonFile.fromJson(it, type)
            data.value = posts
        }
    }

    //получаем список постов
    override fun getAll(): LiveData<List<Post>> = data

    //сохраняем пост
    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = posts + listOf(
                post.copy(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    published = "now"
                )
            )
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }

    //Лайкаем пост
    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (!it.likeByMe) it.likeCount.likeAdd() else it.likeCount.likeDelete()
                it.copy(likeByMe = !it.likeByMe, likeCount = it.likeCount)
            }
        }
        data.value = posts
        sync()
    }

    //поделиться постом
    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                it.share.shareAdd()
                it.copy(shareByMe = !it.shareByMe, share = it.share)
            }
        }
        data.value = posts
        sync()
    }

    //просмотры поста пользователем
    override fun viewById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (!it.viewItByMe) {
                    it.view.viewAdd()
                    it.copy(viewItByMe = !it.viewItByMe, view = it.view)
                } else it
            }
        }
        data.value = posts
        sync()
    }

    //удаление поста
    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    //редактор поста
    override fun edit(post: Post) {
        save(post)
    }

    //функция записи из gson в json
    private fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gsonFile.toJson(posts))
        }
    }
}