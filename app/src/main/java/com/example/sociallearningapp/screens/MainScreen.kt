package com.example.sociallearningapp.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sociallearningapp.viewmodel.ChatViewModel
import com.example.sociallearningapp.viewmodel.QuizViewModel
import com.example.sociallearningapp.viewmodel.TaskViewModel

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Quiz : BottomNavScreen("quiz", "Quiz", Icons.Default.Home)
    object Tasks : BottomNavScreen("tasks", "Tasks", Icons.Default.CheckCircle)
    object Chat : BottomNavScreen("chat", "Chat", Icons.Default.Chat)
}

val bottomNavItems = listOf(
    BottomNavScreen.Quiz,
    BottomNavScreen.Tasks,
    BottomNavScreen.Chat,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    quizViewModel: QuizViewModel,
    taskViewModel: TaskViewModel,
    chatViewModel: ChatViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToQuiz: (Long) -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Social Learning App") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavScreen.Quiz.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Quiz.route) {
                QuizListScreen(viewModel = quizViewModel, onNavigateToQuiz = onNavigateToQuiz)
            }
            composable(BottomNavScreen.Tasks.route) {
                TaskScreen(viewModel = taskViewModel)
            }
            composable(BottomNavScreen.Chat.route) {
                ChatScreen(viewModel = chatViewModel)
            }
        }
    }
}