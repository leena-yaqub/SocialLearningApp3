package com.example.sociallearningapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sociallearningapp.screens.TaskFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(
    currentFilter: TaskFilter,
    onFilterChanged: (TaskFilter) -> Unit,
    taskCounts: Triple<Int, Int, Int>
) {
    Row {
        FilterChip(
            selected = currentFilter == TaskFilter.ALL,
            onClick = { onFilterChanged(TaskFilter.ALL) },
            label = { Text("All (${taskCounts.first})") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = currentFilter == TaskFilter.PENDING,
            onClick = { onFilterChanged(TaskFilter.PENDING) },
            label = { Text("Pending (${taskCounts.second})") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = currentFilter == TaskFilter.COMPLETED,
            onClick = { onFilterChanged(TaskFilter.COMPLETED) },
            label = { Text("Completed (${taskCounts.third})") }
        )
    }
}