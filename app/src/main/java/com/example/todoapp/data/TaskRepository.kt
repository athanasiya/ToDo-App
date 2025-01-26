package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

interface TaskRepository {
//    val tasksStream: Flow<List<Task>>

    fun getAllTasks(): Flow<List<Task>>
    fun getTask(id: Long): Flow<Task?>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}