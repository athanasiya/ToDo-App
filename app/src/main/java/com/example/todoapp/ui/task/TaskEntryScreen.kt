package com.example.todoapp.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.R
import com.example.todoapp.ToDoTopAppBar
import com.example.todoapp.ui.AppViewModelProvider
import com.example.todoapp.ui.navigation.NavigationDestination
import com.example.todoapp.ui.theme.BackgroundColor
import com.example.todoapp.ui.theme.CardColor
import com.example.todoapp.ui.theme.SecondaryColor
import com.example.todoapp.ui.theme.ToDoAppTheme
import kotlinx.coroutines.launch

object TaskEntryDestination : NavigationDestination {
    override val route = "task_entry"
    override val titleRes = R.string.task_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope() // val to launch a coroutine
    var showReminderDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            ToDoTopAppBar(
                title = stringResource(TaskEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Column {
            TaskEntryBody(
                taskUiState = viewModel.taskUiState,
                onTaskValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.saveTask()
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
                    text = stringResource(R.string.set_reminder),
                    color = SecondaryColor
                )
            }
        }
        if (showReminderDialog) {
            ReminderDialogContent(
                onDialogDismiss = { showReminderDialog = false },
                taskTitle = viewModel.taskUiState.taskDetails.title,
                onScheduleReminder = { reminder ->
                    coroutineScope.launch {
                        viewModel.selectReminder(reminder)
                        showReminderDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    onTaskValueChange: (TaskDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TaskInputForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = taskUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CardColor,
                contentColor = SecondaryColor,
                disabledContainerColor = Color.Gray,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@Composable
private fun TaskInputForm(
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = {},
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = taskDetails.title,
            onValueChange = { onValueChange(taskDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.task_title)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SecondaryColor,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = SecondaryColor,
                focusedLabelColor = SecondaryColor,
                cursorColor = CardColor
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = taskDetails.description,
            onValueChange = { onValueChange(taskDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.task_description)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SecondaryColor,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = SecondaryColor,
                focusedLabelColor = SecondaryColor,
                cursorColor = CardColor
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = stringResource(R.string.required),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium)),
                color = SecondaryColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskEntryScreenPreview() {
    ToDoAppTheme {
        TaskEntryBody(taskUiState = TaskUiState(
            TaskDetails(
                title = "Task name", description = "Task Description"
            )
        ), onTaskValueChange = {}, onSaveClick = {})
    }
}