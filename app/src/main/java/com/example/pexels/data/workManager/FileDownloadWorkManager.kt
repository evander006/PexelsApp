package com.example.pexels.data.workManager

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.pexels.R
import com.example.pexels.core.utils.NotificationConstants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject


@HiltWorker
class FileDownloadWorkManager @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString("fileUrl") ?: return Result.failure()
        val fileName = inputData.getString("fileName") ?: "downloaded_file"
        val fileType = inputData.getString("fileType") ?: "PNG"

        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(appContext, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading your file...")
            .setOngoing(true)
            .setProgress(0, 0, true)

        val notificationManager = NotificationManagerCompat.from(appContext)
        notificationManager.notify(NotificationConstants.NOTIFICATION_ID, notificationBuilder.build())

        return try {
            val uri = saveFile(fileName, fileType, fileUrl, appContext)

            notificationBuilder
                .setContentTitle("Download complete")
                .setOngoing(false)
                .setProgress(0, 0, false)

            notificationManager.notify(NotificationConstants.NOTIFICATION_ID, notificationBuilder.build())

            Result.success(workDataOf("uri" to (uri?.toString() ?: "")))
        } catch (e: Exception) {
            e.printStackTrace()

            notificationBuilder
                .setContentTitle("Download failed")
                .setOngoing(false)
                .setProgress(0, 0, false)

            notificationManager.notify(NotificationConstants.NOTIFICATION_ID, notificationBuilder.build())

            Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NotificationConstants.CHANNEL_NAME
            val desc = NotificationConstants.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance).apply {
                description = desc
            }
            val notificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun saveFile(
        fileName: String,
        fileType: String,
        fileUrl: String,
        context: Context
    ): Uri? {
        val mimeType = when (fileType) {
            "PDF" -> "application/pdf"
            "PNG" -> "image/png"
            "MP4" -> "video/mp4"
            else -> return null
        }

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/DownloaderDemo")
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                if (uri != null) {
                    URL(fileUrl).openStream().use { input ->
                        resolver.openOutputStream(uri)?.use { output ->
                            input.copyTo(output, DEFAULT_BUFFER_SIZE)
                        }
                    }
                }
                uri
            } else {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    throw SecurityException("WRITE_EXTERNAL_STORAGE permission not granted")
                }

                val target = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
                )
                URL(fileUrl).openStream().use { input ->
                    FileOutputStream(target).use { output ->
                        input.copyTo(output)
                    }
                }
                target.toUri()
            }
        } catch (se: SecurityException) {
            se.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
