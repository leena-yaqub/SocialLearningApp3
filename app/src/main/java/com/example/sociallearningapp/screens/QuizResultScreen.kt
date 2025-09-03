package com.example.sociallearningapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizResultScreen(
    quizName: String,
    score: Int,
    totalQuestions: Int,
    onSaveAndViewHistory: () -> Unit,
    onRetryQuiz: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Complete!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Your Score",
            fontSize = 18.sp
        )

        Text(
            text = "$score / $totalQuestions",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        val percentage = (score * 100) / totalQuestions
        Text(
            text = "$percentage%",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRetryQuiz,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retry Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}
