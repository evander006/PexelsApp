package com.example.pexels.domain.repository

import com.example.pexels.domain.data.Photo

interface BookmarkPhotoRepository {
    suspend fun getAll(): List<Photo>
    suspend fun insert(photo: Photo)
    suspend fun isBookmarked(photoId: Long): Boolean
    suspend fun delete(photo: Photo)
}