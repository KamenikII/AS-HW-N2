package ru.netology.nmedia.util

import ru.netology.nmedia.dataClasses.Attachment
import ru.netology.nmedia.dataClasses.AttachmentType

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url = url, type = type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}
