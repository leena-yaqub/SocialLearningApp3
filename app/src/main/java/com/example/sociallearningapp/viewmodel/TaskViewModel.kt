package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.Priority
import com.example.sociallearningapp.data.model.Task
import com.example.sociallearningapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val tasks: StateFlow<List<Task>> = repository.getTasksFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addTask(title: String, description: String, priority: Priority) {
        viewModelScope.launch {
            val task = Task(
                id = UUID.randomUUID().toString(),
                userId = auth.currentUser?.uid ?: "",
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun toggleTaskCompleted(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted, updatedAt = System.currentTimeMillis())
            repository.updateTask(updatedTask)
        }
    }
}
