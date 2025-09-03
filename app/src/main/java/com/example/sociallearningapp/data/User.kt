package com.example.sociallearningapp.data

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val quizHistoryScreen: List<QuizHistoryScreen> = emptyList(),
    val tasksCompleted: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

data class QuizHistory(
    val quizId: String = "",
    val quizTitle: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val completedAt: Long = System.currentTimeMillis(),
    val timeSpent: Long = 0
)