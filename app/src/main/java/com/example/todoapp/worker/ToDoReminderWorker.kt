package com.example.todoapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.R

class ToDoReminderWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val taskTitle = inputData.getString(titleKey)

        makeTodoReminderNotification(
            applicationContext.resources.getString(R.string.time_to_do, taskTitle),
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val titleKey = "TASK"
    }
}