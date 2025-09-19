package com.example.pexels.data.repoImpl

import com.example.pexels.data.repository.Api
import com.example.pexels.domain.data.Collection
import com.example.pexels.domain.data.CuratedPhoto
import com.example.pexels.domain.data.Photo
import com.example.pexels.domain.repository.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val api: Api
): PhotoRepository {
    override suspend fun getCuratedPhotos(
        page: Int,
        per_page: Int
    ): List<Photo> =api.getCuratedPhotos(page, per_page).photos

    override suspend fun searchPhotos(query: String): CuratedPhoto {
        return api.searchPhotos(query)
    }

    override suspend fun getFeaturedCollections(
        page: Int,
        per_page: Int
    ): List<Collection> {
        return api.featuredCollections(page, per_page).collections
    }
}