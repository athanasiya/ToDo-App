package com.example.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.todoapp.ui.home.TaskListDestination
import com.example.todoapp.ui.home.TaskListScreen
import com.example.todoapp.ui.task.TaskEditDestination
import com.example.todoapp.ui.task.TaskEditScreen
import com.example.todoapp.ui.task.TaskEntryDestination
import com.example.todoapp.ui.task.TaskEntryScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun ToDoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TaskListDestination.route,
        modifier = modifier
    ) {
        composable(route = TaskListDestination.route) {
            TaskListScreen(
                navigateToTaskEntry = { navController.navigate(TaskEntryDestination.route) },
                navigateToTaskUpdate = { taskId ->
                    println("Navigating to TaskEditScreen with ID: $taskId")
                    navController.navigate("task_edit/$taskId")
                }
            )
        }
        composable(route = TaskEntryDestination.route) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}