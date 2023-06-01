package ru.netology.nmedia.auth

/**КЛАСС ОТВЕЧАЮЩИЙ ЗА СОСТОЯНИЕ АВТОРИЗАЦИИ*/

data class AuthState(val id: Long = 0, val token: String? = null)
