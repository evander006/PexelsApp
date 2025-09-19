package com.example.pexels.data.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pexels.domain.data.Photo
import java.net.URL

@Entity(tableName = "bookmarktable")
data class BookmarkPhotoEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "photographer") val photographer: String?,
    @ColumnInfo(name = "srcOriginal") val srcOriginal: String?
)