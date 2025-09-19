package com.example.pexels.core.nav

sealed class Screens(val route:String) {
    object Home: Screens("home_route")
    object Bookmarks: Screens("bookmark_route")
    object DetailScreen: Screens("detail_route")

}