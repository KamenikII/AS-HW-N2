package ru.netology.nmedia.dao

import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl.Companion.BASE_URL

object FloatingValues {
    var textNewPost = ""
    var currentFragment = ""
    fun renameUrl(nameResource: String, path: String, baseUrl: String = BuildConfig.BASE_URL): String {
        return "$baseUrl/$path/$nameResource"
    }
}