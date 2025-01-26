package com.example.todoapp.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.worker.ToDoReminderWorker
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class WorkManagerToDoRepository(
    context: Context,
    private val taskDao: TaskDao // TaskDao to fetch data from the database
): ToDoRepository {
    private val workManager = WorkManager.getInstance(context)

    override val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    override fun scheduleReminder(duration: Long, unit: TimeUnit, taskTitle: String) {
        // Data.Builder() constructs a Data object (key-value pairs) to pass into the Worker
        val data = Data.Builder() // Data in WorkManager is designed to carry input and output data (int, bool, long, float, double or string)
        data.putString(ToDoReminderWorker.titleKey, taskTitle) // The taskTitle is stored under ToDoReminderWorker.titleKey


        // The builder pattern helps you chain putString(), putInt(), etc., and then finalize with .build() to get a Data instance.
        val workRequestBuilder = OneTimeWorkRequestBuilder<ToDoReminderWorker>()
            .setInitialDelay(duration, unit)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniqueWork(
            taskTitle + duration,
            ExistingWorkPolicy.REPLACE,
            workRequestBuilder
        )
    }
}