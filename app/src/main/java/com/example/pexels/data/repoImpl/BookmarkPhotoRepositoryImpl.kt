package com.example.pexels.data.repoImpl

import com.example.pexels.data.repository.BookmarkPhotoDao
import com.example.pexels.data.roomdb.BookmarkPhotoEntity
import com.example.pexels.domain.data.Photo
import com.example.pexels.domain.data.Src
import com.example.pexels.domain.repository.BookmarkPhotoRepository
import javax.inject.Inject

class BookmarkPhotoRepositoryImpl @Inject constructor(
    private val bookmarkPhotoDao: BookmarkPhotoDao
): BookmarkPhotoRepository {
    override suspend fun getAll(): List<Photo> {
        return bookmarkPhotoDao.getAllBookmarks().map {
            Photo(id = it.id, photographer = it.photographer.toString(), src = Src(original = it.srcOriginal.toString()) )
        }
    }

    override suspend fun insert(photo: Photo) {
        bookmarkPhotoDao.insertBookmark(BookmarkPhotoEntity(photo.id, photo.photographer, photo.src.original))
    }

    override suspend fun isBookmarked(photoId: Long): Boolean = bookmarkPhotoDao.isBookmarked(photoId)

    override suspend fun delete(photo: Photo) {
        bookmarkPhotoDao.deleteBookmark(BookmarkPhotoEntity(photo.id, photo.photographer, photo.src.original))
    }
}