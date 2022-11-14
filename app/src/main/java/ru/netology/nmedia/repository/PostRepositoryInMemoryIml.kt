package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dataClasses.Like
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.dataClasses.Share
import ru.netology.nmedia.dataClasses.View

class PostRepositoryInMemoryIml : PostRepository {
    private var posts = listOf(
        Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
                "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
        published = "7 ноября 2022 в 12:30",
        likeCount = Like(),
        share = Share(),
        view = View()
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
                    "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
            published = "7 ноября 2022 в 12:30",
            likeCount = Like(),
            share = Share(),
            view = View()
        ),
        Post(
            id = 3,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
                    "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
            published = "7 ноября 2022 в 12:30",
            likeCount = Like(),
            share = Share(),
            view = View()
        ),
        Post(
            id = 4,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
                    "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
            published = "7 ноября 2022 в 12:30",
            likeCount = Like(),
            share = Share(),
            view = View()
        ),
        Post(
            id = 5,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Как заставить диджитал-специалиста оторваться от монитора? Попросить его рассказать о своей работе! Так подумали мы и открыли ящик Пандоры — столько интересного узнали, что не успевали записывать. Делимся короткими историями, которые помогут узнать о разных профессиях в диджитале.\n" +
                    "\nБольше 50 профессий на любой вкус и советы, как начать карьеру в диджитале, найдёте на бесплатном курсе «Диджитал-старт»: https://netolo.gy/kOk",
            published = "7 ноября 2022 в 12:30",
            likeCount = Like(),
            share = Share(),
            view = View()
        )
    )

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
}