package com.example.pexels.domain.repository

import java.util.UUID

interface DownloadFileRepository {
    suspend fun downloadFiles(fileType:String,fileName: String, fileUrl: String): UUID
}