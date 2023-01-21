package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dataClasses.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likeCount: Int = 0,
    val shareByMe: Boolean,
    val share: Int = 0,
    val viewItByMe: Boolean,
    val viewIt: Int = 0
) {
    fun toDto() = Post(id, author, content, published, likedByMe, likeCount, shareByMe, share, viewItByMe, viewIt)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likeByMe, dto.likeCount, dto.shareByMe, dto.share, dto.viewItByMe, dto.viewIt)

    }
}