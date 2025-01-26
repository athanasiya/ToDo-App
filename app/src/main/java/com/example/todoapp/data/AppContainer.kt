package com.example.todoapp.data

interface AppContainer {
    val taskRepository: TaskRepository
    val toDoRepository: ToDoRepository
}