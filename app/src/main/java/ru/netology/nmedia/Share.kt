package ru.netology.nmedia

class Share {
    private var shareCount: Long = 0
    private var shareString: String = "0"

    fun shareAdd() {
        shareCount+=1
    }

    override fun toString(): String {
        return when (shareCount) {
            in 0..999 -> "$shareCount"
            in 1000..999_999 -> "${shareCount/1000}K"
            in 1_000_000..999_999_999 -> "${shareCount/1_000_000}M"
            else -> "0"
        }
    }
}