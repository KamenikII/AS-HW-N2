package ru.netology.nmedia.model

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dataClasses.Post

data class FeedModel(
    val posts: LiveData<List<Post>> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
    val refreshing: Boolean = false,
)