package com.example.todoapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.R
import com.example.todoapp.ToDoTopAppBar
import com.example.todoapp.data.Task
import com.example.todoapp.ui.AppViewModelProvider
import com.example.todoapp.ui.navigation.NavigationDestination
import com.example.todoapp.ui.theme.BackgroundColor
import com.example.todoapp.ui.theme.CardColor
import com.example.todoapp.ui.theme.SecondaryColor
import com.example.todoapp.ui.theme.ToDoAppTheme

object TaskListDestination : NavigationDestination {
    override val route = "task_list"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToTaskUpdate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = viewModel(factory = AppViewModelProvider.Factory) // new parameter of type TaskListViewModel
) {
    val taskListUiState by viewModel.taskListUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        containerColor = BackgroundColor,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ToDoTopAppBar(
                title = stringResource(TaskListDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                containerColor = SecondaryColor
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.task_entry_title)
                )
            }
        },
    ) { innerPadding ->
        TaskListBody(
            taskList = taskListUiState.taskList, // pass in taskListUiState.taskList to the taskList parameter, now task list displays if you saved tasks in your app database
            onTaskClick = { taskId ->
                navigateToTaskUpdate(taskId)
            },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun TaskListBody(
    taskList: List<Task>,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_task),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
                color = SecondaryColor
            )
        } else {
            TaskList(
                taskList = taskList,
                onTaskClick = { taskId ->
                    onTaskClick(taskId)
                },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun TaskList(
    taskList: List<Task>,
    onTaskClick: (Long) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = taskList, key = { it.id }) { task ->
            TaskItem(task = task,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable {
                        onTaskClick(task.id)
                    }
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 4.dp,
            bottomStart = 4.dp,
            bottomEnd = 24.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFE1E4D9)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.titleMedium,
                    color = SecondaryColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    ToDoAppTheme {
        TaskListBody(listOf
            (
            Task(1, "Task 1", "desc"),
            Task(2, "Task 2", "desc2"),
            Task(3, "Task 3", "desc3")
        ),
            onTaskClick = {}

        )
    }
}