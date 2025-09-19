package com.example.pexels.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CuratedPhoto(
    val next_page: String = "",
    val page: Int = 0,
    val per_page: Int = 0,
    val photos: List<Photo> = emptyList()
): Parcelable
@Parcelize
data class Photo(
    val alt: String = "",
    val avg_color: String = "",
    val height: Int = 0,
    val id: Long = 0L,
    val liked: Boolean = false,
    val photographer: String = "",
    val photographer_id: Long = 0L,
    val photographer_url: String = "",
    val src: Src = Src(),
    val url: String = "",
    val width: Int = 0
): Parcelable

@Parcelize
data class Src(
    val landscape: String = "",
    val large: String = "",
    val large2x: String = "",
    val medium: String = "",
    val original: String = "",
    val portrait: String = "",
    val small: String = "",
    val tiny: String = ""
): Parcelable
