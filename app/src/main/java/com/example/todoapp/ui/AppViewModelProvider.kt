package com.example.todoapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todoapp.ToDoApplication
import com.example.todoapp.ui.home.TaskListViewModel
import com.example.todoapp.ui.task.TaskEditViewModel
import com.example.todoapp.ui.task.TaskEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire ToDo App
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for TaskListViewModel
        initializer {
            TaskListViewModel(toDoApplication().container.taskRepository)
        }

        // Initializer for TaskEntryViewModel
        initializer {
            TaskEntryViewModel(
                toDoApplication().container.taskRepository,
                toDoApplication().container.toDoRepository
            )
        }

        // Initializer for TaskEditViewModel
        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle(),
                toDoApplication().container.taskRepository,
                toDoApplication().container.toDoRepository
            )
        }
    }
}

fun CreationExtras.toDoApplication(): ToDoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ToDoApplication)
