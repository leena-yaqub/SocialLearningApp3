package com.example.sociallearningapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sociallearningapp.viewmodel.QuizViewModel

@Composable
fun QuizListScreen(
    viewModel: QuizViewModel,
    onNavigateToQuiz: (Long) -> Unit
) {
    val quizzes by viewModel.availableQuizzes.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quizzes) { quiz ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToQuiz(quiz.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = quiz.name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = quiz.description)
                }
            }
        }
    }
}
