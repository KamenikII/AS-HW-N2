package ru.netology.nmedia.dto

/**КЛАСС, НЕОБХОДИМЫЙ ДЛЯ АВТОРИЗАЦИИ И ПР */

data class Token(
    val id: Long,
    val token: String,
    val avatar: String? = null,
)
