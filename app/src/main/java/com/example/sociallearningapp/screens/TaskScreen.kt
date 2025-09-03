package com.example.sociallearningapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sociallearningapp.data.model.Task
import com.example.sociallearningapp.ui.components.AddEditTaskDialog
import com.example.sociallearningapp.viewmodel.TaskViewModel
import androidx.compose.ui.text.style.TextAlign
enum class TaskFilter { ALL, PENDING, COMPLETED }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var currentFilter by remember { mutableStateOf(TaskFilter.ALL) }
    var showDeleteDialog by remember { mutableStateOf<Task?>(null) }

    val tasks by viewModel.allTasks.collectAsState()

    val filteredTasks = remember(tasks, currentFilter) {
        when (currentFilter) {
            TaskFilter.ALL -> tasks
            TaskFilter.PENDING -> tasks.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tasks") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingTask = null
                    showDialog = true
                },
                containerColor = Color(0xFF03DAC5)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Filter chips
            FilterChips(
                currentFilter = currentFilter,
                onFilterChanged = { currentFilter = it },
                taskCounts = Triple(
                    tasks.size,
                    tasks.count { !it.isCompleted },
                    tasks.count { it.isCompleted }
                )
            )

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (currentFilter) {
                            TaskFilter.ALL -> "No tasks yet.\nTap + to add a new task."
                            TaskFilter.PENDING -> "No pending tasks!"
                            TaskFilter.COMPLETED -> "No completed tasks yet."
                        },
                        fontSize = 18.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
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
                            onDelete = { showDeleteDialog = task },
                            onToggle = { viewModel.toggleTask(task) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddEditTaskDialog(
            task = editingTask,
            onDismiss = { showDialog = false },
            onSave = { task ->
                if (editingTask != null) viewModel.updateTask(task)
                else viewModel.addTask(task)
                showDialog = false
            }
        )
    }

    showDeleteDialog?.let { task ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete '${task.title}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTask(task.id)
                        showDeleteDialog = null
                    }
                ) { Text("Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancel") }
            }
        )
    }
}
