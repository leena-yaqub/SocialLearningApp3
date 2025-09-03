package com.example.sociallearningapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sociallearningapp.data.SampleQuizData

@Composable
fun QuizDetailScreen(
    quizName: String,
    onNavigateBack: () -> Unit,
    onQuizComplete: (score: Int) -> Unit
) {
    var currentQuestion by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf(-1) }

    // Use actual quiz data from SampleQuizData
    val questions = when (quizName.lowercase()) {
        "math quiz" -> SampleQuizData.getMathQuestions()
        "science quiz" -> SampleQuizData.getScienceQuestions()
        "history quiz" -> SampleQuizData.getHistoryQuestions()
        else -> SampleQuizData.getGeneralKnowledgeQuestions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = quizName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (currentQuestion < questions.size) {
            val currentQ = questions[currentQuestion]

            Text(
                text = "Question ${currentQuestion + 1} of ${questions.size}",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = currentQ.question,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Display all options dynamically
            currentQ.options.forEachIndexed { index, option ->
                Button(
                    onClick = {
                        selectedAnswer = index
                        if (index == currentQ.correctAnswer) {
                            score++
                        }
                        // Move to next question or complete quiz
                        if (currentQuestion < questions.size - 1) {
                            currentQuestion++
                            selectedAnswer = -1
                        } else {
                            onQuizComplete(score)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(option)
                }
            }
        }
    }
}