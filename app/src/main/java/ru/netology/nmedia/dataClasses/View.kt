package ru.netology.nmedia.dataClasses

class View {
    private var viewCount: Long = 0
    private var viewString: String = "0"

    fun viewAdd() {
        viewCount+=1
    }

    override fun toString(): String {
        return when (viewCount) {
            in 0..999 -> "$viewCount"
            in 1000..999_999 -> "${viewCount/1000}K"
            in 1_000_000..999_999_999 -> "${viewCount/1_000_000}M"
            else -> "0"
        }
    }
}