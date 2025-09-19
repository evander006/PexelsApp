package com.example.pexels.domain.repository

import com.example.pexels.domain.data.Collection
import com.example.pexels.domain.data.CuratedPhoto
import com.example.pexels.domain.data.FeaturedCollections
import com.example.pexels.domain.data.Photo

interface PhotoRepository {
    suspend fun getCuratedPhotos(page:Int, per_page: Int): List<Photo>
    suspend fun searchPhotos(query: String): CuratedPhoto
    suspend fun getFeaturedCollections(page:Int, per_page: Int): List<Collection>
}