package ru.netology.nmedia.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dataClasses.Post
import java.io.IOException

/**КЛАСС, ДЛЯ ПОЛУЧЕНИЯ ПОСТОВ ПО СТРАНИЦАМ*/

class PostPagingSource(
    private val apiService: ApiService,
) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {

        try {
            val result = when (params) {
                //бновление страницы
                is LoadParams.Refresh -> {
                    apiService.getLatest(params.loadSize)
                }
                //скролл вниз
                is LoadParams.Append -> {
                    apiService.getBefore(
                        id = params.key,
                        count = params.loadSize,
                    )
                }
                //скролл вверх
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(),
                    nextKey = null,
                    prevKey = params.key,
                )
                else -> TODO()
            }

            if (!result.isSuccessful) {
                throw HttpException(result)
            }

            val data = result.body().orEmpty()
            return LoadResult.Page(
                data = data,
                prevKey = params.key,
                nextKey = data.lastOrNull()?.id,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}