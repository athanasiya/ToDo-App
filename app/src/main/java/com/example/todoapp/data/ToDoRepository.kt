package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

interface ToDoRepository {
    fun scheduleReminder(duration: Long, unit: TimeUnit, taskTitle: String)
    val tasks: Flow<List<Task>>
}