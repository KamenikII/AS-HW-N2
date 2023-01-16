package ru.netology.nmedia.repository

import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {
    //получаем список постов
    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }

    //сохраняем пост
    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    //удаление поста
    override fun removeById(id: Long) {
        dao.removeById(id)
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