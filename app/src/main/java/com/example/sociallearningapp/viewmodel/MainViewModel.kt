package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // Quiz History State
    private val _quizHistory = MutableStateFlow<List<QuizHistory>>(emptyList())
    val quizHistory: StateFlow<List<QuizHistory>> = _quizHistory.asStateFlow()

    // Tasks State
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _tasksCompleted = MutableStateFlow(0)
    val tasksCompleted: StateFlow<Int> = _tasksCompleted.asStateFlow()

    // User creation date (use epoch millis)
    private val _createdAt = MutableStateFlow(System.currentTimeMillis())
    val createdAt: StateFlow<Long> = _createdAt.asStateFlow()

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load sample data
                loadSampleQuizHistory()
                loadSampleTasks()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadSampleQuizHistory() {
        val now = System.currentTimeMillis()
        val sampleHistory = listOf(
            QuizHistory(
                id = 1,
                quizId = 1,
                quizName = "Math Basics",
                score = 8,
                maxScore = 10,
                completedAt = now - 86_400_000L // Yesterday
            ),
            QuizHistory(
                id = 2,
                quizId = 2,
                quizName = "Science Quiz",
                score = 7,
                maxScore = 10,
                completedAt = now - 172_800_000L // 2 days ago
            ),
            QuizHistory(
                id = 3,
                quizId = 3,
                quizName = "History Challenge",
                score = 9,
                maxScore = 10,
                completedAt = now - 259_200_000L // 3 days ago
            )
        )
        _quizHistory.value = sampleHistory
    }

    private fun loadSampleTasks() {
        val now = System.currentTimeMillis()
        val sampleTasks = listOf(
            Task(
                id = "1",
                userId = "sample-user",
                title = "Complete Math Assignment",
                description = "Finish chapter 5 exercises",
                priority = Priority.HIGH,
                isCompleted = true,
                createdAt = now - 86_400_000L
            ),
            Task(
                id = "2",
                userId = "sample-user",
                title = "Read Science Article",
                description = "Read about quantum physics",
                priority = Priority.MEDIUM,
                isCompleted = false,
                createdAt = now - 172_800_000L
            ),
            Task(
                id = "3",
                userId = "sample-user",
                title = "Practice Vocabulary",
                description = "Learn 20 new words",
                priority = Priority.LOW,
                isCompleted = true,
                createdAt = now - 259_200_000L
            )
        )
        _tasks.value = sampleTasks
        _tasksCompleted.value = sampleTasks.count { it.isCompleted }
    }

    fun addQuizResult(quizName: String, score: Int, maxScore: Int) {
        viewModelScope.launch {
            val newHistory = QuizHistory(
                id = _quizHistory.value.size + 1L,
                quizId = _quizHistory.value.size + 1L,
                quizName = quizName,
                score = score,
                maxScore = maxScore,
                completedAt = System.currentTimeMillis()
            )
            _quizHistory.value = _quizHistory.value + newHistory
        }
    }

    fun toggleTaskComplete(task: Task) {
        viewModelScope.launch {
            val updatedTasks = _tasks.value.map {
                if (it.id == task.id) {
                    it.copy(isCompleted = !it.isCompleted)
                } else {
                    it
                }
            }
            _tasks.value = updatedTasks
            _tasksCompleted.value = updatedTasks.count { it.isCompleted }
        }
    }

    fun addTask(title: String, description: String, priority: Priority) {
        viewModelScope.launch {
            val newTask = Task(
                id = (_tasks.value.size + 1).toString(),
                userId = "sample-user",
                title = title,
                description = description,
                priority = priority,
                isCompleted = false,
                createdAt = System.currentTimeMillis()
            )
            _tasks.value = _tasks.value + newTask
        }
    }
}
