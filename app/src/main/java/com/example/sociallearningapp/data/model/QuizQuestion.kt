package com.example.sociallearningapp.data.model

data class QuizQuestion(
    val id: Long = 0,
    val quizId: Long = 0,
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: Int = 0
)