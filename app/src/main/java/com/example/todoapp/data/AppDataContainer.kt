package com.example.todoapp.data

import android.content.Context

class AppDataContainer(private val context: Context): AppContainer {
    override val taskRepository: TaskRepository by lazy {
        TaskLocalRepository(AppDatabase.getDatabase(context).taskDao())
    }

    override val toDoRepository: ToDoRepository by lazy {
        WorkManagerToDoRepository(context, AppDatabase.getDatabase(context).taskDao())
    }
}