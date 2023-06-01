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
    val authorId: Long,
    val shareByMe: Boolean = false,
    val share: Int = 0,
    val viewItByMe: Boolean = false,
    val viewIt: Int = 0,
    val urlOfVideo: String? = null,
    val hidden: Boolean = false,
    @Embedded
    val attachment: AttachmentEmbeddable?,
) {
    fun toDto() = Post(
        id = id,
        author = author,
        authorImage= authorImage,
        content = content,
        authorId = authorId,
        published = published,
        urlOfVideo = urlOfVideo,
        attachment = attachment?.toDto(),
        likeByMe = likedByMe,
        likes = likeCount,
        shareByMe = shareByMe,
        share = share,
        viewItByMe = viewItByMe,
        viewIt = viewIt,
        hidden = hidden
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                id = dto.id,
                author = dto.author,
                authorImage= dto.authorImage,
                content = dto.content,
                authorId = dto.authorId,
                published = dto.published,
                urlOfVideo = dto.urlOfVideo,
                attachment = AttachmentEmbeddable.fromDto(dto.attachment),
                likedByMe = dto.likeByMe,
                likeCount = dto.likes,
                shareByMe = dto.shareByMe,
                share = dto.share,
                viewItByMe = dto.viewItByMe,
                viewIt = dto.viewIt,
                hidden = dto.hidden
            )
    }
}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)