package com.example.pexels.presentation.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexels.domain.data.Collection
import com.example.pexels.domain.data.Photo
import com.example.pexels.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
): ViewModel() {
    private val _curedPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val curedPhotos=_curedPhotos.asStateFlow()

//    private val _isLoading= MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()

    private val _uiState= MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val uiState=_uiState.asStateFlow()
    private val _featuredCollections= MutableStateFlow<List<Collection>>(emptyList())
    val featuredCollections=_featuredCollections.asStateFlow()

    init {
        getCuredPhotos()
        getCollections()
    }
    internal fun getCuredPhotos(page:Int=1, per_page:Int=30){
        viewModelScope.launch {
            _uiState.value= HomeUIState.Loading
            try {
                val res=photoRepository.getCuratedPhotos(page,per_page)
                _uiState.value = HomeUIState.Success(res)
                Log.d("viewmodel","$res")

            }catch (e: IOException){
                _uiState.value= HomeUIState.Failure("Try again")
                Log.e("viewmodel","${e.message}")
            }
        }
    }
    internal fun search(query: String){
        viewModelScope.launch {
            if (query.isBlank()) return@launch
            _uiState.value= HomeUIState.Loading
            try {
                val res=photoRepository.searchPhotos(query)
                _uiState.value= HomeUIState.Success(res.photos)
            }catch(e:Exception){
                _uiState.value= HomeUIState.Failure("Search failure")
                Log.e("viewmodel","Search error ${e.message}")
            }
        }
    }
    internal fun getCollections(page:Int=1, per_page:Int=30){
        viewModelScope.launch {
            try {
                val resCollections=photoRepository.getFeaturedCollections(page, per_page)
                _featuredCollections.value=resCollections
            }catch (e: Exception){
                Log.e("viewmodel","Collections error ${e.message}")
            }
        }
    }

    sealed class HomeUIState{
        object Loading: HomeUIState()
        data class Success(val photos:List<Photo>): HomeUIState()
        data class Failure(val message: String): HomeUIState()
        //data class FeaturedCollections(val featuredCollections:List<Collection>): HomeUIState()
    }
}

