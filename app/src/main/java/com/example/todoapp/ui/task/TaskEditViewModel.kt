package com.example.todoapp.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Reminder
import com.example.todoapp.data.TaskRepository
import com.example.todoapp.data.ToDoRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val toDoRepository: ToDoRepository
): ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private val taskId: Long = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])

    init {
        viewModelScope.launch {
            taskUiState = taskRepository.getTask(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(true)
        }
    }

    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState = TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }

    suspend fun updateTask() {
        if (validateInput(taskUiState.taskDetails)) {
            taskRepository.updateTask(taskUiState.taskDetails.toTask())
        }
    }

    suspend fun deleteTask() {
        taskRepository.deleteTask(taskUiState.taskDetails.toTask())
    }

    fun scheduleTaskReminder(reminder: Reminder) {
        toDoRepository.scheduleReminder(reminder.duration, reminder.unit, reminder.taskTitle)
    }
}
