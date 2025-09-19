package com.example.pexels.presentation.detailScreen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pexels.R
import com.example.pexels.domain.data.Photo
import com.example.pexels.presentation.bookmarksScreen.BookmarkViewModel
import com.example.pexels.presentation.detailScreen.DetailScreenViewModel.DownloadState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenUI(photo: Photo?, downloadViewModel: DetailScreenViewModel= hiltViewModel(),
                   bookmarkViewModel: BookmarkViewModel=hiltViewModel(),
                   goBackToHomeScreenUI:()-> Unit) {
    val downloadState by downloadViewModel.downloadFile.collectAsState()
    val bookmarkedPhotoIds by bookmarkViewModel.bookmarkedPhotosIds.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        goBackToHomeScreenUI()
                    },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF3F5F9)),
                        modifier = Modifier.border(border = BorderStroke(0.dp, Color.Transparent), shape = RoundedCornerShape(12.dp)),
                    ) {
                        Icon(
                            painterResource(R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(photo?.photographer.toString())
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        photo?.let {
                            downloadViewModel.downloadFile(
                                fileType = "PNG",
                                fileName = "photo_${it.id}.png",
                                fileUrl = it.src.original
                            )
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF3F5F9)
                    )

                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icondownlaod),
                            tint = Color.Unspecified,
                            contentDescription = "Download"
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Download", color = Color(0xFF1E1E1E))
                    }
                }
                photo?.let {
                    IconButton(onClick = {
                        bookmarkViewModel.toggleBookmark(it)
                    },
                        modifier = Modifier.border(border = BorderStroke(0.dp, Color.Transparent),RoundedCornerShape(200.dp)),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFF3F5F9)
                        )
                    ) {
                        Icon(
                            tint = Color.Unspecified,
                            painter =if (bookmarkedPhotoIds.contains(it.id)) painterResource(R.drawable.bookmarkactive) else painterResource(R.drawable.bookmarkinactive),
                            contentDescription = "Bookmark"
                        )
                    }
                }

            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Photo
                AsyncImage(
                    model = photo?.src?.original,
                    contentDescription = photo?.alt,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (downloadState) {
                    is DownloadState.Success -> {
                        Toast.makeText(LocalContext.current, "Download completed", Toast.LENGTH_LONG).show()
                    }
                    is DownloadState.Failure -> {
                        Toast.makeText(LocalContext.current, "Download failed", Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }

            if (downloadState is DownloadState.InProgress) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F5F9), shape = RoundedCornerShape(12.dp)) // blue background
                        .padding(12.dp)
                        .align(Alignment.TopCenter)
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = Color(0xFFBB1020)
                    )
                }
            }
        }
    }
}