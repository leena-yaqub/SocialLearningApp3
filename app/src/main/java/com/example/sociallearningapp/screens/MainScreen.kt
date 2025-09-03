package com.example.sociallearningapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsState
import com.example.sociallearningapp.data.model.*
import com.example.sociallearningapp.ui.components.*
import com.example.sociallearningapp.ui.theme.*
import com.example.sociallearningapp.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

// Main Screen with navigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToQuiz: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val quizHistory by viewModel.quizHistory.collectAsState()
    val tasksCompleted by viewModel.tasksCompleted.collectAsState()
    val createdAt by viewModel.createdAt.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Welcome section
        Text(
            text = "Welcome back!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(
                title = "Tasks Completed",
                value = tasksCompleted.toString(),
                modifier = Modifier.weight(1f)
            )

            StatsCard(
                title = "Quizzes Taken",
                value = quizHistory.size.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent quiz history
        Text(
            text = "Recent Quiz History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (quizHistory.isEmpty()) {
            EmptyState(
                title = "No quiz history",
                description = "Complete your first quiz to see your progress here"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quizHistory.take(5)) { quiz ->
                    QuizHistoryItem(
                        quiz = quiz,
                        onQuizClick = { onNavigateToQuiz(quiz.quizName) }
                    )
                }
            }
        }
    }
}

// Quiz Detail Screen (Fixed overload issue)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailScreen(
    quizName: String,
    onNavigateBack: () -> Unit,
    onQuizComplete: (score: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentQuestion by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    // Sample quiz questions
    val questions = remember {
        listOf(
            QuizQuestion("What is 2 + 2?", listOf("3", "4", "5", "6"), 1),
            QuizQuestion("What is the capital of France?", listOf("London", "Berlin", "Paris", "Madrid"), 2),
            QuizQuestion("What is 10 × 3?", listOf("30", "20", "40", "25"), 0)
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(quizName) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (isCompleted) {
            // Quiz completed screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Quiz Completed!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Score: $score/${questions.size}",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        onQuizComplete(score)
                        onNavigateBack()
                    }
                ) {
                    Text("Continue")
                }
            }
        } else {
            // Quiz questions
            QuizQuestionContent(
                question = questions[currentQuestion],
                questionNumber = currentQuestion + 1,
                totalQuestions = questions.size,
                onAnswerSelected = { selectedAnswer ->
                    if (selectedAnswer == questions[currentQuestion].correctAnswer) {
                        score++
                    }

                    if (currentQuestion < questions.size - 1) {
                        currentQuestion++
                    } else {
                        isCompleted = true
                    }
                }
            )
        }
    }
}

// Quiz History Screen (Fixed overload issue)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHistoryScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quizHistory by viewModel.quizHistory.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Quiz History") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (quizHistory.isEmpty()) {
            EmptyState(
                title = "No quiz history",
                description = "Complete your first quiz to see your progress here"
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quizHistory) { quiz ->
                    QuizHistoryItem(
                        quiz = quiz,
                        onQuizClick = { }
                    )
                }
            }
        }
    }
}

// Task Screen with UI components
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Active", "Completed", "High Priority")
    val filteredTasks = when (selectedFilter) {
        "Active" -> tasks.filter { !it.isCompleted }
        "Completed" -> tasks.filter { it.isCompleted }
        "High Priority" -> tasks.filter { it.priority == Priority.HIGH }
        else -> tasks
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Tasks") },
            actions = {
                IconButton(onClick = { /* Add task */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add task")
                }
            }
        )

        // Filter chips
        FilterChips(
            filters = filters,
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tasks list
        if (filteredTasks.isEmpty()) {
            EmptyState(
                title = "No tasks",
                description = "Add your first task to get started"
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTasks) { task ->
                    TaskItem(
                        task = task,
                        onTaskClick = { },
                        onToggleComplete = { viewModel.toggleTaskComplete(it) }
                    )
                }
            }
        }
    }
}

// Helper composables
@Composable
private fun StatsCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = LightBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Orange
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuizHistoryItem(
    quiz: QuizHistory,
    onQuizClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onQuizClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quiz.quizName,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        .format(quiz.completedAt),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${quiz.score}/${quiz.maxScore}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Orange
            )
        }
    }
}

@Composable
private fun QuizQuestionContent(
    question: QuizQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress indicator
        LinearProgressIndicator(
            progress = { questionNumber.toFloat() / totalQuestions },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Question $questionNumber of $totalQuestions",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = question.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Answer options
        question.options.forEachIndexed { index, option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                onClick = { onAnswerSelected(index) }
            ) {
                Text(
                    text = option,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}