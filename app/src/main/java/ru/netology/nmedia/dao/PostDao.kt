package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ru.netology.nmedia.entity.PostEntity
import kotlinx.coroutines.flow.Flow

/** ОБЪЕКТ ДЛЯ РАБОТЫ С БД И СЕРВЕРОМ */

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE hidden = 0 ORDER BY id DESC")
    fun getAllVisible(): Flow<List<PostEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    suspend fun updateContentById(id: Long, content: String)

    suspend fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

    @Query("""
        UPDATE PostEntity SET
        likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    suspend fun likeById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("""
                UPDATE PostEntity SET
                    share = share + CASE WHEN shareByMe THEN -1 ELSE 1 END,
                    shareByMe = CASE WHEN shareByMe THEN 0 ELSE 1 END
                WHERE id = :id
            """)
    suspend fun shareById(id: Long)

    @Query("""
                UPDATE PostEntity SET
                    viewIt = viewIt + CASE WHEN viewItByMe THEN -1 ELSE 1 END,
                    viewItByMe = CASE WHEN viewItByMe THEN 0 ELSE 1 END
                WHERE id= :id
            """)
    suspend fun viewItById(id: Long)
}