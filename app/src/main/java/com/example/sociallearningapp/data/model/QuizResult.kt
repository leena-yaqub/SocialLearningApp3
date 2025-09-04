package com.example.sociallearningapp.data.model

data class QuizResult(
    val id: Long = 0,
    val userId: String = "",
    val quizId: Long = 0,
    val quizName: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val timeSpent: Long = 0L
)
