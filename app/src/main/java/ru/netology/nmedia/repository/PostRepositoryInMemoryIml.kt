package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dataClasses.Like
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.dataClasses.Share
import ru.netology.nmedia.dataClasses.View
import ru.netology.nmedia.presentation.PostRepository

class PostRepositoryInMemoryIml : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
                "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
        published = "7 ноября 2022 в 12:30",
        likeCount = Like(),
        share = Share(),
        view = View()
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        if (!post.likeByMe) post.likeCount.likeAdd() else post.likeCount.likeDelete()
        post = post.copy(likeByMe = !post.likeByMe, likeCount = post.likeCount)
        data.value = post
    }
    override fun share() {
        post.share.shareAdd()
        post = post.copy(shareByMe = !post.shareByMe, share = post.share)
        data.value = post
    }

    override fun view() {
        if (!post.viewItByMe) {
            post.view.viewAdd()
            post = post.copy(viewItByMe = !post.viewItByMe, view = post.view)
        }
        data.value = post
    }
}