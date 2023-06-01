package ru.netology.nmedia.dataClasses

//files
data class Attachment(
    val url: String, //URL приложения
    val description: String? = "", //описание
    val type: AttachmentType, //тип
)