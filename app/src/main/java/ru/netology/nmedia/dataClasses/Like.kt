package ru.netology.nmedia.dataClasses

class Like {
    private var likeCount: Long = 0
    private var likeString: String = "0"

    fun likeAdd() {
        likeCount += 1
    }

    fun likeDelete() {
        if (likeCount>0) likeCount -= 1
    }

    override fun toString(): String {
        return when (likeCount) {
            in 0..999 -> "$likeCount"
            in 1000..999_999 -> "${likeCount/1000}K"
            in 1_000_000..999_999_999 -> "${likeCount/1_000_000}M"
            else -> "0"
        }
    }

}