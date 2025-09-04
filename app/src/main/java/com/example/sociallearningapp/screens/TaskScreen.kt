package com.example.sociallearningapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sociallearningapp.data.model.Task
import com.example.sociallearningapp.ui.components.AddEditTaskDialog
import com.example.sociallearningapp.ui.components.FilterChips
import com.example.sociallearningapp.ui.components.TaskItem
import com.example.sociallearningapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var currentFilter by remember { mutableStateOf(TaskFilter.ALL) }

    val tasks by viewModel.tasks.collectAsState()

    val filteredTasks = when (currentFilter) {
        TaskFilter.ALL -> tasks
        TaskFilter.PENDING -> tasks.filter { !it.isCompleted }
        TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingTask = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            FilterChips(
                currentFilter = currentFilter,
                onFilterChanged = { currentFilter = it },
                taskCounts = Triple(
                    tasks.size,
                    tasks.count { !it.isCompleted },
                    tasks.count { it.isCompleted }
                )
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTasks) { task ->
                    TaskItem(
                        task = task,
                        onEdit = {
                            editingTask = task
                            showDialog = true
                        },
                        onDelete = { viewModel.deleteTask(task.id) },
                        onToggle = { viewModel.toggleTaskCompleted(task) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddEditTaskDialog(
            task = editingTask,
            onDismiss = { showDialog = false },
            onSave = { task ->
                if (editingTask == null) {
                    viewModel.addTask(task.title, task.description, task.priority)
                } else {
                    viewModel.updateTask(task)
                }
                showDialog = false
            }
        )
    }
}
