package com.example.sociallearningapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val quizId: Long,                 // used in QuizViewModel
    val quizName: String,
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long = System.currentTimeMillis(), // when the attempt finished
    val timeSpent: Long = 0L                            // optional, ms
)
