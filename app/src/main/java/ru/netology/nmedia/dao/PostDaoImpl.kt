package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dataClasses.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    //получаем список постов
    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            //курсором пробегаемся по строкам БД
            while(it.moveToNext()) posts.add(map(it))
        }
        return posts
    }

    override fun save(post: Post): Post { //сохраняем пост
        val values = ContentValues().apply {
            if (post.id != 0L) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            if (post.author.isNotBlank()) {
                put(PostColumns.COLUMN_AUTHOR, post.author)
            }
            if (post.published.isNotBlank()) {
                put(PostColumns.COLUMN_PUBLISHED, post.published)
            }
            if (post.content.isNotBlank()) {
                put(PostColumns.COLUMN_CONTENT, post.content)
            }
            put(PostColumns.COLUMN_LIKED_BY_ME, post.likeByMe)
            put(PostColumns.COLUMN_LIKES, post.likeCount)
            put(PostColumns.COLUMN_SHARED_BY_ME, post.shareByMe)
            put(PostColumns.COLUMN_SHARES, post.share)
            put(PostColumns.COLUMN_VIEW_BY_ME, post.viewItByMe)
            put(PostColumns.COLUMN_VIEWS, post.viewIt)
        }

        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE} SET
                    ${PostColumns.COLUMN_LIKES} = ${PostColumns.COLUMN_LIKES} + CASE WHEN ${PostColumns.COLUMN_LIKED_BY_ME} THEN -1 ELSE 1 END,
                    ${PostColumns.COLUMN_LIKED_BY_ME} = CASE WHEN ${PostColumns.COLUMN_LIKED_BY_ME} THEN 0 ELSE 1 END
                WHERE id=?
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE} SET
                    ${PostColumns.COLUMN_SHARES} = ${PostColumns.COLUMN_SHARES} + CASE WHEN ${PostColumns.COLUMN_SHARED_BY_ME} THEN -1 ELSE 1 END,
                    ${PostColumns.COLUMN_SHARED_BY_ME} = CASE WHEN ${PostColumns.COLUMN_SHARED_BY_ME} THEN 0 ELSE 1 END
                WHERE id=?
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun viewById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE} SET
                    ${PostColumns.COLUMN_VIEWS} = ${PostColumns.COLUMN_VIEWS} + CASE WHEN ${PostColumns.COLUMN_VIEW_BY_ME} THEN -1 ELSE 1 END,
                    ${PostColumns.COLUMN_VIEW_BY_ME} = CASE WHEN ${PostColumns.COLUMN_VIEW_BY_ME} THEN 0 ELSE 1 END
                WHERE id=?
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun edit(post: Post) {
        save(post)
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likeByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likeCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                shareByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARED_BY_ME)) != 0,
                share = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)),
                viewItByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEW_BY_ME)) != 0,
                viewIt = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWS))
            )
        }
    }
}