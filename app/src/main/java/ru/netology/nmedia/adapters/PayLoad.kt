package ru.netology.nmedia.adapters

data class PayLoad(
    val likeByMe: Boolean? = null,
    val content: String? = null,
    val image: String?=null, // для рекламы
)
