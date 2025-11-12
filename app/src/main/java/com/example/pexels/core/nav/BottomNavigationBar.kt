package com.example.pexels.core.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pexels.domain.data.Photo
import com.example.pexels.presentation.bookmarksScreen.BookmarksScreenUI
import com.example.pexels.presentation.detailScreen.DetailScreenUI
import com.example.pexels.presentation.homeScreen.HomeScreenUI


@Composable
fun BottomNavigationBar(paddingValues: PaddingValues) {
    var navigationSelectedItem by remember {
        mutableStateOf(0)
    }
    val navController= rememberNavController()
    val backStackEntry=navController.currentBackStackEntryAsState()
    val currDestination=backStackEntry.value?.destination?.route
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            if (currDestination!= Screens.DetailScreen.route)
            NavigationBar(containerColor = Color.White) {
                BottomNavigationItem().getNavigationItems().forEachIndexed { ind, item->
                    NavigationBarItem(
                        selected = ind==navigationSelectedItem,
                        onClick = {
                            navigationSelectedItem=ind
                            navController.navigate(item.route){
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState=true
                                }
                                launchSingleTop=true
                                restoreState=true
                            }
                        },
                        icon = {
                            val iconRes=if (ind==navigationSelectedItem) item.activeicon else item.inactiveicon
                            Icon(painterResource(iconRes),null,tint = Color.Unspecified)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = Color.Unspecified,
                            unselectedIconColor = Color.Unspecified
                        )
                    )
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Screens.Home.route){
            composable(Screens.Home.route) {
                HomeScreenUI(
                    goToDetailScreen ={
                        navController.currentBackStackEntry?.savedStateHandle?.set("photo", it)
                        navController.navigate(Screens.DetailScreen.route)
                    }
                )
            }
            composable(Screens.Bookmarks.route) {
                BookmarksScreenUI(goToDetailScreen = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("photo", it)
                    navController.navigate(Screens.DetailScreen.route) },
                    goToHomeScreen = {
                        navController.navigate(Screens.Home.route)
                    })
            }
            composable(Screens.DetailScreen.route) {
                val photo=navController.previousBackStackEntry?.savedStateHandle?.get<Photo>("photo")
                DetailScreenUI(photo){
                    navController.navigate(Screens.Home.route){
                        popUpTo(Screens.Home.route){
                            inclusive=true
                        }
                    }
                }
            }
        }
    }
}