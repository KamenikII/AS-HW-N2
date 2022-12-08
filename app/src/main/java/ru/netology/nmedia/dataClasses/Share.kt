package ru.netology.nmedia.dataClasses

import ru.netology.nmedia.util.Numbers

class Share {
    private var shareCount: Long = 0
    private var shareString: String = "0"

    fun shareAdd() {
        shareCount+=1
    }

    override fun toString(): String { return Numbers.changeNumbers(shareCount) }
}