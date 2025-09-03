// File: app/src/main/java/com/example/sociallearningapp/data/DataClasses.kt
package com.example.sociallearningapp.data

// User data class
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "avatar_0"
)

// Quiz data class
data class Quiz(
    val name: String,
    val duration: String,
    val description: String
)

// Quiz Question data class
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)

// Chat Message data class
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

// Bottom Navigation Item
data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

// Onboarding Page data class
data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color
)

// Quiz History Item
data class QuizHistoryItem(
    val quizName: String,
    val score: String,
    val timestamp: String,
    val color: androidx.compose.ui.graphics.Color
)