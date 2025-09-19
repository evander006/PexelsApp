package com.example.pexels.data.repository

import com.example.pexels.domain.data.CuratedPhoto
import com.example.pexels.domain.data.FeaturedCollections
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("curated")
    suspend fun getCuratedPhotos(
        @Query("page") page:Int=1,
        @Query("per_page") per_page:Int=30
    ): CuratedPhoto

    @GET("search")
    suspend fun searchPhotos(
        @Query("query") query:String=""
    ): CuratedPhoto

    @GET("collections/featured")
    suspend fun featuredCollections(
        @Query("page") page:Int=1,
        @Query("per_page") per_page:Int=30
    ): FeaturedCollections
}