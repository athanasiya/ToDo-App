package com.example.todoapp.ui.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.FIVE_SECONDS
import com.example.todoapp.ONE_DAY
import com.example.todoapp.ONE_MINUTE
import com.example.todoapp.R
import com.example.todoapp.SEVEN_DAYS
import com.example.todoapp.THIRTY_DAYS
import com.example.todoapp.THIRTY_SECONDS
import com.example.todoapp.TWO_MINUTES
import com.example.todoapp.ToDoTopAppBar
import com.example.todoapp.data.Reminder
import com.example.todoapp.ui.AppViewModelProvider
import com.example.todoapp.ui.navigation.NavigationDestination
import com.example.todoapp.ui.theme.BackgroundColor
import com.example.todoapp.ui.theme.ErrorColor
import com.example.todoapp.ui.theme.SecondaryColor
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object TaskEditDestination : NavigationDestination {
    override val route = "task_edit"
    override val titleRes = R.string.task_edit_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) } // state to control delete dialog visibility
    var showReminderDialog by remember { mutableStateOf(false) } // state to control reminder dialog visibility

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            ToDoTopAppBar(
                title = stringResource(TaskEditDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            TaskEntryBody(
                taskUiState = viewModel.taskUiState,
                onTaskValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateTask()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        top = innerPadding.calculateTopPadding()
                    )
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium),
                    )
                    .fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = ErrorColor,
                    contentColor = SecondaryColor
                )
            ) {
                Text(stringResource(R.string.delete))
            }
            OutlinedButton(
                onClick = { showReminderDialog = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium),
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.edit_reminder),
                    color = SecondaryColor
                )
            }
        }
        if (showDeleteDialog) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    showDeleteDialog = false
                    coroutineScope.launch {
                        viewModel.deleteTask()
                        navigateBack()
                    }
                },
                onDeleteCancel = { showDeleteDialog = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }

        if (showReminderDialog) {
            ReminderDialogContent(
                onDialogDismiss = { showReminderDialog = false },
                taskTitle = viewModel.taskUiState.taskDetails.title,
                onScheduleReminder = { reminder ->
                    coroutineScope.launch {
                        viewModel.scheduleTaskReminder(reminder)
                        showReminderDialog = false
                        navigateBack()
                    }
                }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}

@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    taskTitle: String,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    val reminders = listOf(
        Reminder(R.string.five_seconds, FIVE_SECONDS, TimeUnit.SECONDS, taskTitle),
        Reminder(R.string.thirty_seconds, THIRTY_SECONDS, TimeUnit.SECONDS, taskTitle),
        Reminder(R.string.one_minute, ONE_MINUTE, TimeUnit.MINUTES, taskTitle),
        Reminder(R.string.two_minutes, TWO_MINUTES, TimeUnit.MINUTES, taskTitle),
        Reminder(R.string.one_day, ONE_DAY, TimeUnit.DAYS, taskTitle),
        Reminder(R.string.one_week, SEVEN_DAYS, TimeUnit.DAYS, taskTitle),
        Reminder(R.string.one_month, THIRTY_DAYS, TimeUnit.DAYS, taskTitle)
    )

    AlertDialog(
        onDismissRequest = { onDialogDismiss() },
        confirmButton = {},
        title = { Text(stringResource(R.string.remind_me, taskTitle)) },
        text = {
            Column {
                reminders.forEach {
                    Text(
                        text = stringResource(it.durationRes),
                        modifier = Modifier
                            .clickable {
                                onScheduleReminder(it)
                                onDialogDismiss()
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        },
        modifier = modifier
    )
}