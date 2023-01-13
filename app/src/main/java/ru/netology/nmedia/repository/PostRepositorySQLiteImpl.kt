package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dataClasses.Post

class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    //получаем список постов
    override fun getAll(): LiveData<List<Post>> = data

    //сохраняем пост
    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        if (id == 0L) {
            posts = posts + listOf(saved)
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    //удаление поста
    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }


    //Лайкаем пост
    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likeByMe = !it.likeByMe,
                likeCount = if (it.likeByMe) it.likeCount-1 else it.likeCount+1
            )
        }
        data.value = posts
    }

    //поделиться постом
    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id != id) it else {
                it.share + 1
                it.copy(shareByMe = !it.shareByMe, share = it.share)
            }
        }
        data.value = posts
    }

    //просмотры поста пользователем
    override fun viewById(id: Long) {
        dao.viewById(id)
        posts = posts.map {
            if (it.id != id) it else {
                if (!it.viewItByMe) {
                    it.viewIt + 1
                    it.copy(viewItByMe = !it.viewItByMe, viewIt = it.viewIt)
                } else it
            }
        }
        data.value = posts
    }

    //редактор поста
    override fun edit(post: Post) {
        save(post)
    }
}