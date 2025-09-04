package com.example.sociallearningapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val questions: List<QuizQuestion> = emptyList(),
    val category: String,
    val createdAt: Long = System.currentTimeMillis()
)
