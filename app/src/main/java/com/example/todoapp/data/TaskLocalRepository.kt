package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

class TaskLocalRepository(private val taskDao: TaskDao): TaskRepository {
//    override val tasksStream: Flow<List<Task>> = taskDao.getAllTasks()

    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getTask(id: Long): Flow<Task?> = taskDao.getTask(id)

    override suspend fun addTask(task: Task) = taskDao.insertTask(task)

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}