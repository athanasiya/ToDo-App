package com.example.todoapp.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.Reminder
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskRepository
import com.example.todoapp.data.ToDoRepository

class TaskEntryViewModel(
    private val taskRepository: TaskRepository,
    private val toDoRepository: ToDoRepository
): ViewModel() {
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    var pendingReminder: Reminder? = null
        private set

    // validation for form input values
    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }

    // updates the taskUiState with the value provided in the argument
    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState = TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    fun selectReminder(reminder: Reminder) {
        pendingReminder = reminder
    }

    // saveTask() is used to insert the task into Room database
    suspend fun saveTask() {
        if(validateInput()) {
            taskRepository.addTask(taskUiState.taskDetails.toTask())
            scheduleTaskReminder(pendingReminder)
            pendingReminder = null
        }
    }

    private fun scheduleTaskReminder(reminder: Reminder?) {
        if (reminder == null) {
            return
        }
        toDoRepository.scheduleReminder(reminder.duration, reminder.unit, reminder.taskTitle)
    }
}

// Ui State for an Item
data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Long = 0,
    val title: String = "",
    val description: String = ""
)

fun TaskDetails.toTask(): Task = Task( // Extension function to convert TaskDetails to Task
    id = id,
    title = title,
    description = description
)

fun Task.toTaskDetails(): TaskDetails = TaskDetails( // Extension function to convert Task to TaskDetails
    id = id,
    title = title,
    description = description
)

fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState( // Extension function to convert Task to TaskUiState
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)