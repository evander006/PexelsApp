package com.example.pexels.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pexels.data.repository.BookmarkPhotoDao

@Database(entities = [BookmarkPhotoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkPhotoDao(): BookmarkPhotoDao
}