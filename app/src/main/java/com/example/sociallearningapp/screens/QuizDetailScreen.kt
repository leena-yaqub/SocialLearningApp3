package com.example.sociallearningapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sociallearningapp.viewmodel.QuizViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizDetailScreen(
    quizViewModel: QuizViewModel,
    quizId: Long,
    onNavigateBack: () -> Unit,
    onQuizComplete: () -> Unit
) {
    LaunchedEffect(quizId) {
        quizViewModel.startQuiz(quizId)
    }

    val questions by quizViewModel.questions.collectAsState()
    val currentQuestionIndex by quizViewModel.currentQuestionIndex.collectAsState()
    val timeRemaining by quizViewModel.timeRemaining.collectAsState()
    val isQuizFinished by quizViewModel.isQuizFinished.collectAsState()

    LaunchedEffect(currentQuestionIndex) {
        while (timeRemaining > 0) {
            delay(1000)
            quizViewModel.decrementTime()
        }
    }

    if (isQuizFinished) {
        LaunchedEffect(Unit) {
            onQuizComplete()
        }
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentQuestion != null) {
            Text(
                text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Time Remaining: $timeRemaining",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            currentQuestion.options.forEachIndexed { index, option ->
                Button(
                    onClick = { quizViewModel.submitAnswer(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = option)
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}