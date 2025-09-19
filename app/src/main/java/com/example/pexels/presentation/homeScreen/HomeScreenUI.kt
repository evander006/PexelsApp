package com.example.pexels.presentation.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.pexels.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pexels.domain.data.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUI(
    homeViewModel: HomeViewModel=hiltViewModel(),
    goToDetailScreen:(photo: Photo)-> Unit,

){
    val uiState by homeViewModel.uiState.collectAsState()
    val featuredCollections by homeViewModel.featuredCollections.collectAsState()
    val photosCured by homeViewModel.curedPhotos.collectAsState()
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var chosenElementCollection by remember { mutableStateOf("") }
    Column {
        SearchBar(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(50.dp), // rounded corners
            colors = SearchBarDefaults.colors(
                containerColor = Color(0xFFF3F5F9), // background
                dividerColor = Color.Transparent,
                inputFieldColors = SearchBarDefaults.inputFieldColors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedLeadingIconColor = Color(0xFFBB1020),   // search icon color
                    unfocusedLeadingIconColor = Color(0xFFBB1020),
                    focusedPlaceholderColor = Color(0xFF868686),   // hint color
                    unfocusedPlaceholderColor = Color(0xFF868686)
                )
            ),
            query = query,
            onQueryChange = {
                query=it
            },
            onSearch = {
                active=false
                homeViewModel.search(query)
            },
            active = false,
            onActiveChange = {
               //active=it
            },
            placeholder = {
                Text(
                    text = "Search",
                    color = Color(0xFF868686) // hint color
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFFBB1020) // search icon color
                )
            }
        ) {
        }
        Spacer(Modifier.height(24.dp))

        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(featuredCollections.take(7)) {
                Button(
                    onClick = {
                        chosenElementCollection=it.id
                        homeViewModel.search(it.title)
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chosenElementCollection==it.id) Color(0xFFBB1020) else Color(0xFFF3F5F9),
                        contentColor = if (chosenElementCollection==it.id) Color.White else Color(0xFF1E1E1E)
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        when(uiState){
            is HomeViewModel.HomeUIState.Failure -> {
            //    val error = (uiState as HomeViewModel.HomeUIState.Failure).message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.nonetwork),
                            contentDescription = "No connection",
                            tint = Color.Unspecified,
                        )
                        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), onClick = { homeViewModel.getCuredPhotos() }) {
                            Text("Try Again", color = Color(0xFFBB1020))
                        }
                    }
                }
            }
            HomeViewModel.HomeUIState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }
            is HomeViewModel.HomeUIState.Success -> {
                val photos = (uiState as HomeViewModel.HomeUIState.Success).photos
                LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    items(photos) {photo->
                        PhotoItem(photo){
                            goToDetailScreen(it)
                        }

                    }
                }
            }
        }

    }

}

@Composable
fun PhotoItem(photo: Photo, onClick:(photo: Photo)-> Unit){
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable{
                onClick(photo)
            }
    ) {
        AsyncImage(
            model = photo.src.original,
            contentDescription = photo.alt,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder)
        )
    }
}