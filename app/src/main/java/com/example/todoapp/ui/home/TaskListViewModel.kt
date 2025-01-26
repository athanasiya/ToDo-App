package com.example.todoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TaskListViewModel(taskRepository: TaskRepository): ViewModel () {
    val taskListUiState: StateFlow<TaskListUiState> = taskRepository.getAllTasks().map { TaskListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TaskListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TaskListUiState(val taskList: List<Task> = listOf())
