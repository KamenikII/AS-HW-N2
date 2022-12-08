package ru.netology.nmedia.util

object Numbers {
    fun changeNumbers(count: Long): String {
        return when (count) {
            in 0..999 -> "$count"
            in 1000..999_999 -> "${count / 1000}K"
            in 1_000_000..999_999_999 -> "${count / 1_000_000}M"
            else -> "0"
        }
    }
}