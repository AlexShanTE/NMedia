package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.nmedia.data.dto.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun add(post: PostEntity)

    @Update
    fun edit(post: PostEntity)

    @Query(
        """
        UPDATE posts SET
            likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id;
            """
    )
    fun likedById(id: Long)

    @Query(
        """
        UPDATE posts SET
            shares = shares + 1
        WHERE id = :id ;
        """
    )
    fun shareById(id: Long)

    @Query("DELETE FROM posts WHERE id=:id")
    fun delete(id: Long)

}