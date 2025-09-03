package com.example.sociallearningapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val duration: String,
    val description: String,
    val category: String = "General",
    val createdAt: Long = System.currentTimeMillis() // KEEP so you can sort by newest
)
