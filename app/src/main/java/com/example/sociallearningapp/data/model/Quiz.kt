package com.example.sociallearningapp.data.model

data class Quiz(
    val id: Long = 0,
    val name: String = "",
    val duration: String = "",
    val description: String = "",
    val category: String = "General",
    val createdAt: Long = System.currentTimeMillis()
)
