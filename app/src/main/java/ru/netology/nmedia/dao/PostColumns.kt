package ru.netology.nmedia.dao

object PostColumns {
    const val TABLE = "posts"
    const val COLUMN_ID = "id"
    const val COLUMN_AUTHOR = "author"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_PUBLISHED = "published"
    const val COLUMN_LIKED_BY_ME = "likeByMe"
    const val COLUMN_LIKES = "likeCount"
    const val COLUMN_SHARED_BY_ME = "shareByMe"
    const val COLUMN_SHARES = "share"
    const val COLUMN_VIEW_BY_ME = "viewItByMe"
    const val COLUMN_VIEWS = "viewIt"
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