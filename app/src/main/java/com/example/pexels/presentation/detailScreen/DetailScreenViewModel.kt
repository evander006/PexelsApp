package com.example.pexels.presentation.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.pexels.data.repoImpl.DownloadFileRepositoryImpl
import com.example.pexels.data.workManager.FileDownloadWorkManager
import com.example.pexels.domain.data.Photo
import com.example.pexels.domain.repository.DownloadFileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val downloadFileRepository: DownloadFileRepository,
    private val workManager: WorkManager
): ViewModel() {
    private val _downloadFile= MutableStateFlow<DownloadState>(DownloadState.Idle)
    val downloadFile=_downloadFile.asStateFlow()

    fun downloadFile(fileType: String, fileName: String, fileUrl: String){
        viewModelScope.launch {
            try {
                _downloadFile.value= DownloadState.InProgress
                val workId=downloadFileRepository.downloadFiles(fileType,fileName,fileUrl)
                workManager.getWorkInfoByIdLiveData(workId).observeForever {
                    if (it!=null){
                        when(it.state){
                            WorkInfo.State.RUNNING -> {
                                _downloadFile.value = DownloadState.InProgress
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                val uri = it.outputData.getString("uri") ?: ""
                                _downloadFile.value= DownloadState.Success(uri)
                            }
                            WorkInfo.State.FAILED -> {
                                _downloadFile.value= DownloadState.Failure("Download failed")
                            }
                            else -> {}
                        }
                    }
                }

            }catch (e: Exception){
                _downloadFile.value= DownloadState.Failure(e.message.toString())
            }
        }
    }

    sealed class DownloadState{
        object Idle: DownloadState()
        object InProgress: DownloadState()
        data class Success(val uri: String): DownloadState()
        data class Failure(val message: String): DownloadState()


    }
}