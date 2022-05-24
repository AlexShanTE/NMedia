package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import ru.netology.nmedia.data.dto.Post

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {
    override fun getAll(): List<Post> {
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            null, null, null, null,
            "${PostsTable.Column.ID.columnName} DESC"
        ).use { cursor ->
            List(cursor.count) {
                cursor.moveToNext()
                cursor.toPost()
            }
        }
    }

    override fun add(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, post.author)
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.VIDEO_CONTENT.columnName, post.videoContent)
        }

        val id = db.insert(PostsTable.NAME,null,values)

        val cursor = db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (cursor.count == 0) error("Exception")
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun edit(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, post.author)
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.VIDEO_CONTENT.columnName, post.videoContent)
        }
        db.update(
            PostsTable.NAME,
            values,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(post.id.toString())
        )
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?", arrayOf(post.id.toString()),
            null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun likedById(id: Long) {
        db.execSQL(
            """
            UPDATE ${PostsTable.NAME} SET
                likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
            UPDATE ${PostsTable.NAME} SET
                shares = shares + 1
            WHERE id = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

}