package ru.netology.nmedia.adapters

import ru.netology.nmedia.dataClasses.Post

/** ИНТЕРФЕЙС ОТВЕЧАЮЩИЙ ЗА МЕТОДЫ, КОТОРЫЕ "СЛУШАЮТ" НАЖАТИЯ ПОЛЬЗОВАТЕЛЯ*/

interface OnPostListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun view(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onPlayVideo(post: Post) {}
    fun onPreviewPost(post: Post) {}
    fun onPost(id: Long) {}
    fun onReload() {}
}