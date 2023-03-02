package ru.netology.nmedia.dao

import ru.netology.nmedia.BuildConfig

object FloatingValues {
    var textNewPost = ""
    var currentFragment = ""
    fun renameUrl(nameResource: String, path: String, baseUrl: String = BuildConfig.BASE_URL): String {
        return "$baseUrl/$path/$nameResource"
    }
}