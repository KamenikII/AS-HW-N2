package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dataClasses.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorImage: String? = null,
    val content: String,
    val published: String,
    val likedByMe: Boolean= false,
    val likes: Int = 0,
    val sharedByMe: Boolean = false,
    val countShared: Int = 0,
    val viewedByMe: Boolean = false,
    val countViews: Int = 0,
    val urlOfVideo: String? = null
) {
    fun toPost() = Post(
        id, author, authorImage, content, published, likedByMe, likes,
        sharedByMe, countShared, viewedByMe, countViews, urlOfVideo
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id, dto.author, dto.authorImage, dto.content, dto.published, dto.likeByMe, dto.likes,
                dto.shareByMe, dto.share, dto.viewItByMe, dto.viewIt, dto.urlOfVideo
            )
    }
}