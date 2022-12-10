package ru.netology.nmedia.dataClasses

import ru.netology.nmedia.util.Numbers

class View {
    private var viewCount: Long = 0
    private var viewString: String = "0"

    fun viewAdd() {
        viewCount+=1
    }

    override fun toString(): String { return Numbers.changeNumbers(viewCount) }
}