package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.User
import com.example.sociallearningapp.data.repository.QuizRepository
import com.example.sociallearningapp.data.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val quizRepository: QuizRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _quizHistoryCount = MutableStateFlow(0)
    val quizHistoryCount: StateFlow<Int> = _quizHistoryCount

    private val _tasksCompletedCount = MutableStateFlow(0)
    val tasksCompletedCount: StateFlow<Int> = _tasksCompletedCount

    init {
        loadUserProfile()
        loadQuizHistoryCount()
        loadTasksCompletedCount()
    }

    private fun loadUserProfile() {
        auth.currentUser?.uid?.let { userId ->
            database.getReference("users").child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        _user.value = snapshot.getValue(User::class.java)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    private fun loadQuizHistoryCount() {
        viewModelScope.launch {
            auth.currentUser?.uid?.let { userId ->
                quizRepository.getQuizHistory(userId).collect {
                    _quizHistoryCount.value = it.size
                }
            }
        }
    }

    private fun loadTasksCompletedCount() {
        viewModelScope.launch {
            taskRepository.getTasksFlow().collect { tasks ->
                _tasksCompletedCount.value = tasks.count { it.isCompleted }
            }
        }
    }
}
