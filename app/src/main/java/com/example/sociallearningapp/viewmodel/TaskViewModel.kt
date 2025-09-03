// File: app/src/main/java/com/example/sociallearningapp/viewmodel/TaskViewModel.kt
package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.Task
import com.example.sociallearningapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            try {
                repository.getTasksFlow().collect { taskList ->
                    _allTasks.value = taskList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load tasks: ${e.message}"
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.addTask(task)
                if (!success) {
                    _errorMessage.value = "Failed to add task"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error adding task: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.updateTask(task)
                if (!success) {
                    _errorMessage.value = "Failed to update task"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error updating task: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.deleteTask(taskId)
                if (!success) {
                    _errorMessage.value = "Failed to delete task"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting task: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            updateTask(updatedTask)
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
