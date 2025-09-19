package com.example.pexels.presentation.bookmarksScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.example.pexels.domain.data.Photo
import com.example.pexels.domain.repository.BookmarkPhotoRepository
import com.example.pexels.presentation.homeScreen.HomeViewModel.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkPhotoRepository: BookmarkPhotoRepository
): ViewModel() {
    private val _bookmarkPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val bookmarkPhotos = _bookmarkPhotos.asStateFlow()
    private val _bookmarkedPhotosIds= MutableStateFlow<Set<Long>>(emptySet())
    val bookmarkedPhotosIds=_bookmarkedPhotosIds.asStateFlow()
    init {
        showAllBookmarks()
    }
    fun showAllBookmarks(){
        viewModelScope.launch {
            val photos=bookmarkPhotoRepository.getAll()
            _bookmarkPhotos.value=photos
            _bookmarkedPhotosIds.value=photos.map { it.id }.toSet()
        }
    }
    fun toggleBookmark(photo: Photo){
        viewModelScope.launch {
            if (bookmarkPhotoRepository.isBookmarked(photo.id)){
                bookmarkPhotoRepository.delete(photo)
            }else{
                bookmarkPhotoRepository.insert(photo)
            }
            showAllBookmarks()
        }
    }

    fun refresh(){
        showAllBookmarks()
    }
}