package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks") // the default tableName will be the name of the data class
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String
)
