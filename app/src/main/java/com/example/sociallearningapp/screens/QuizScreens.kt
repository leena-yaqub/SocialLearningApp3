// File: app/src/main/java/com/example/sociallearningapp/screens/QuizScreens.kt
package com.example.sociallearningapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sociallearningapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ---------------------------
// Minimal data models (move to data package if you already have them)
// ---------------------------
data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)

data class Quiz(
    val name: String,
    val duration: String = "5 questions • 10 sec each",
    val questions: List<Question>
)

data class QuizHistoryItem(val quizName: String, val score: String, val timestamp: String, val color: Color)

// Sample data (for demo). Replace with your QuizData source when ready.
object QuizData {
    val quizzes: List<Quiz> = listOf(
        Quiz(
            name = "General Knowledge",
            questions = listOf(
                Question("Which city is the capital of Australia?", listOf("Sydney", "Melbourne", "Canberra", "Perth"), 2),
                Question("Which is the largest planet in our solar system?", listOf("Earth", "Jupiter", "Saturn", "Neptune"), 1),
                Question("Who wrote the play Romeo and Juliet?", listOf("Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"), 1),
                Question("What is the smallest prime number?", listOf("0", "1", "2", "3"), 2),
                Question("Mount Everest lies on the border between which two countries?", listOf("India & Pakistan", "Nepal & China", "Nepal & India", "China & Mongolia"), 1)
            )
        ),
        Quiz(
            name = "Social Science",
            questions = listOf(
                Question("What does GDP stand for?", listOf("Gross Domestic Product", "General Domestic Product", "Gross Development Percentage", "General Development Product"), 0),
                Question("Who is famous for the theory of natural selection?", listOf("Isaac Newton","Albert Einstein","Charles Darwin","Sigmund Freud"), 2),
                Question("Which branch of government makes laws?", listOf("Judiciary","Executive","Legislature","Bureaucracy"), 2),
                Question("Which field studies markets and resource choices?", listOf("Sociology","Economics","Anthropology","Political Science"), 1),
                Question("What is socialization?", listOf("Isolating individuals","Learning social norms","Government welfare","Study of social media"), 1)
            )
        ),
        Quiz(
            name = "Computer Science",
            questions = listOf(
                Question("What does CPU stand for?", listOf("Central Programming Unit","Central Processing Unit","Computer Processing Utility","Core Processing Unit"), 1),
                Question("Which component manages hardware & services to apps?", listOf("Compiler","Operating System","Database","Firmware"), 1),
                Question("Which language defines structure of web pages?", listOf("CSS","JavaScript","HTML","Python"), 2),
                Question("What does RAM stand for and property?", listOf("Read-Access Memory - non-volatile","Random Access Memory - volatile","Rapid Access Memory - permanent","Random Allocation Memory - cloud"), 1),
                Question("Which data structure is LIFO?", listOf("Queue","Stack","Tree","Graph"), 1)
            )
        )
    )
}

// ---------------------------
// Quiz History Screen
// ---------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHistoryScreen(
    onNavigateBack: () -> Unit
) {
    val historyItems = remember {
        listOf(
            QuizHistoryItem("Math Quiz", "85%", "2 days ago", Color(0xFF4CAF50)),
            QuizHistoryItem("Science Quiz", "92%", "4 days ago", Color(0xFF2196F3)),
            QuizHistoryItem("History Quiz", "78%", "1 week ago", Color(0xFFFF9800))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBackground)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historyItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(item.color.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Use an emoji instead of a material icon to avoid missing-icon issues
                            Text(text = "🎓", fontSize = 20.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.quizName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = item.timestamp,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = item.score,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = item.color
                            )
                            // use simple glyph instead of Chevron/KeyboardArrowRight icon
                            Text(
                                text = "›",
                                fontSize = 20.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------
// Quiz Detail/Taking Screen
// ---------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailScreen(
    quizName: String,
    onNavigateBack: () -> Unit,
    onQuizComplete: (Int) -> Unit
) {
    val quiz = QuizData.quizzes.find { it.name == quizName }
    if (quiz == null) {
        // fallback UI if quiz isn't found
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Quiz") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryPurple,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Quiz not found", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onNavigateBack) { Text("Back") }
            }
        }
        return
    }

    // Local UI state
    val questions: List<Question> = quiz.questions
    var questionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf(-1) }
    var isAnswered by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(10) } // seconds per question

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Start / reset timer on each question change
    LaunchedEffect(questionIndex) {
        selectedOptionIndex = -1
        isAnswered = false
        timeLeft = 10

        while (timeLeft > 0 && !isAnswered) {
            delay(1000L)
            timeLeft -= 1
        }

        if (!isAnswered && timeLeft <= 0) {
            // timeout: show snackbar and move to next question after brief pause
            snackbarHostState.showSnackbar("Time's up!")
            delay(600L)
            if (questionIndex < questions.size - 1) {
                questionIndex += 1
            } else {
                onQuizComplete(score)
            }
        }
    }

    // suspend function to handle selection (we call it from coroutineScope.launch)
    suspend fun handleSelectionSuspend(optionIndex: Int) {
        if (isAnswered) return
        isAnswered = true
        selectedOptionIndex = optionIndex

        val currentQuestion = questions.getOrNull(questionIndex)
        val correctIndex = currentQuestion?.correctIndex ?: -1

        if (optionIndex == correctIndex) {
            score += 1
        }

        // show feedback for 700ms then move to next
        delay(700L)

        if (questionIndex < questions.size - 1) {
            questionIndex += 1
        } else {
            onQuizComplete(score)
        }
    }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(quiz.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        val currentQuestion = questions.getOrNull(questionIndex)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LightBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top row: progress + timer
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Q ${questionIndex + 1} / ${questions.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    // time text
                    Text(
                        text = "${timeLeft}s",
                        fontWeight = FontWeight.SemiBold,
                        color = if (timeLeft <= 3) Color.Red else Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // linear progress showing remaining time (corrected)
                LinearProgressIndicator(
                    progress = (timeLeft / 10f).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question content
            if (currentQuestion != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    // Question text
                    Text(
                        text = currentQuestion.text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Options
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        currentQuestion.options.forEachIndexed { idx, optionText ->
                            val isSelected = selectedOptionIndex == idx
                            val correctIndex = currentQuestion.correctIndex
                            val optionColor = when {
                                isAnswered && idx == correctIndex -> Color(0xFF4CAF50) // correct -> green
                                isAnswered && isSelected && idx != correctIndex -> Color(0xFFF44336) // wrong -> red
                                else -> Color.White
                            }
                            val borderColor = when {
                                isAnswered && idx == correctIndex -> Color(0xFF4CAF50)
                                isAnswered && isSelected && idx != correctIndex -> Color(0xFFF44336)
                                else -> Color(0xFFDDDDDD)
                            }

                            Button(
                                onClick = {
                                    // call the suspend handler inside a coroutine
                                    coroutineScope.launch {
                                        handleSelectionSuspend(idx)
                                    }
                                },
                                enabled = !isAnswered,
                                colors = ButtonDefaults.buttonColors(containerColor = optionColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, borderColor)
                            ) {
                                Text(
                                    text = optionText,
                                    color = if (optionColor == Color.White) Color.Black else Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            } else {
                // fallback if question missing
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No question available", fontSize = 18.sp)
                }
            }

            // Bottom area: small hint + score preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Auto-advance after answer or timeout", fontSize = 12.sp, color = Color.Gray)
                Text("Score: $score", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
