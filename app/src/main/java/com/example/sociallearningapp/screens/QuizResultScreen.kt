package com.example.sociallearningapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sociallearningapp.viewmodel.QuizViewModel

@Composable
fun QuizResultScreen(
    viewModel: QuizViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val score = viewModel.getScore()
    val totalQuestions = viewModel.questions.value.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Quiz Complete!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your score: $score / $totalQuestions", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateToHistory) {
            Text("View History")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToHome) {
            Text("Back to Home")
        }
    }
}
