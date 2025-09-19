package com.example.pexels.core.nav

import com.example.pexels.R

data class BottomNavigationItem(
    val inactiveicon:Int=0,
    val activeicon:Int=0,
    val route: String= Screens.Home.route
) {
    fun getNavigationItems(): List<BottomNavigationItem>{
        return listOf(
            BottomNavigationItem(R.drawable.homebtninactive,R.drawable.homebtnactive, Screens.Home.route),
            BottomNavigationItem(R.drawable.bookmarkinactive, R.drawable.bookmarkactive,Screens.Bookmarks.route)
        )
    }

}