package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.util.AttachmentEmbeddable

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorImage: String? = null,
    val content: String,
    val published: String,
    val likedByMe: Boolean= false,
    val likeCount: Int = 0,
    val shareByMe: Boolean = false,
    val share: Int = 0,
    val viewItByMe: Boolean = false,
    val viewIt: Int = 0,
    val urlOfVideo: String? = null,
    @Embedded
    val attachment: AttachmentEmbeddable?,
) {
    fun toPost() = Post(
        id, author, authorImage, content, published, urlOfVideo, attachment?.toDto(), likedByMe, likeCount,
        shareByMe, share, viewItByMe, viewIt
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id, dto.author, dto.authorImage, dto.content, dto.published, dto.likeByMe, dto.likes,
                dto.shareByMe, dto.share, dto.viewItByMe, dto.viewIt, dto.urlOfVideo, AttachmentEmbeddable.fromDto(dto.attachment)
            )
    }
}