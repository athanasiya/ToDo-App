package com.example.todoapp.ui.navigation

interface NavigationDestination {
    val route: String // unique name to define the path for a composable
    val titleRes: Int // string resource id that contains title to be displayed for the screen
}