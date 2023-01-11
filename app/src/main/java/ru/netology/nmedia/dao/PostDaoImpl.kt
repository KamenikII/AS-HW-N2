package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dataClasses.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        //строим таблицу БД
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE} ( 
                ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
                ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
                ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
                ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_SHARED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_VIEW_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0
            );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likeByMe"
        const val COLUMN_LIKES = "likeCount"
        const val COLUMN_SHARED_BY_ME = "shareByMe"
        const val COLUMN_SHARES = "shareIt"
        const val COLUMN_VIEW_BY_ME = "viewItByMe"
        const val COLUMN_VIEWS = "view"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_SHARED_BY_ME,
            COLUMN_SHARES,
            COLUMN_VIEW_BY_ME,
            COLUMN_VIEWS
        )
    }

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

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            // TODO: remove hardcoded values
            put(PostColumns.COLUMN_AUTHOR, "Netology")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "now")
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
                UPDATE posts SET
                    likeCount = likeCount + CASE WHEN likeByMe THEN -1 ELSE 1 END,
                    likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE id = ?;
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
                UPDATE posts SET
                    share = share + CASE WHEN shareByMe THEN -1 ELSE 1 END,
                    shareByMe = 1 
                WHERE id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun viewById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                    viewIt = viewIt + CASE WHEN viewItByMe THEN -1 ELSE 1 END,
                    viewIt = 1 
                WHERE id = ?;
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
                shareByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                share = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                viewItByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                viewIt = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES))
            )
        }
    }
}