package ru.netology.nmedia.dataClasses

import ru.netology.nmedia.util.Numbers

class Like {
    private var likeCount: Long = 0
    private var likeString: String = "0"

    fun likeAdd() {
        likeCount += 1
    }

    fun likeDelete() {
        if (likeCount>0) likeCount -= 1
    }

    override fun toString(): String { return Numbers.changeNumbers(likeCount) }
}