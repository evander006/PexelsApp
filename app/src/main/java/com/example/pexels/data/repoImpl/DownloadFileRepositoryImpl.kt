package com.example.pexels.data.repoImpl

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.pexels.data.workManager.FileDownloadWorkManager
import com.example.pexels.domain.repository.DownloadFileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class DownloadFileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): DownloadFileRepository {
    override suspend fun downloadFiles(
        fileType: String,
        fileName: String,
        fileUrl: String
    ): UUID {
        val workManager = WorkManager.getInstance(context)
        val data= workDataOf(
            "fileUrl" to fileUrl,
            "fileName" to fileName,
            "fileType" to fileType
        )
        val request = OneTimeWorkRequestBuilder<FileDownloadWorkManager>()
            .setInputData(data)
            .build()
        workManager.enqueue(request)
        return request.id
    }

}