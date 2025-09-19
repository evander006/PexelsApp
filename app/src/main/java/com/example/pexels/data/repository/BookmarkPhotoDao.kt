package com.example.pexels.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pexels.data.roomdb.BookmarkPhotoEntity


@Dao
interface BookmarkPhotoDao {
    @Query("SELECT * FROM bookmarktable")
    suspend fun getAllBookmarks(): List<BookmarkPhotoEntity>
    @Query("SELECT EXISTS(SELECT 1 FROM bookmarktable WHERE id = :photoId)")
    suspend fun isBookmarked(photoId: Long): Boolean
    @Insert
    suspend fun insertBookmark(photoEntity: BookmarkPhotoEntity)
    @Delete
    suspend fun deleteBookmark(photoEntity: BookmarkPhotoEntity)
}