package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dataClasses.Like
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.dataClasses.Share
import ru.netology.nmedia.dataClasses.View

class PostRepositoryInMemoryIml : PostRepository {
    private var nextId = 1L
    private var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (!it.likeByMe) it.likeCount.likeAdd() else it.likeCount.likeDelete()
                it.copy(likeByMe = !it.likeByMe, likeCount = it.likeCount)
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                it.share.shareAdd()
                it.copy(shareByMe = !it.shareByMe, share = it.share)
            }
        }
        data.value = posts
    }

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
    }

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
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}