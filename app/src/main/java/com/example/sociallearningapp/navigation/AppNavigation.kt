package com.example.sociallearningapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sociallearningapp.screens.*
import com.example.sociallearningapp.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        // Main Screen
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onNavigateToQuiz = { quizName ->
                    navController.navigate("quiz_detail/$quizName")
                }
            )
        }

        // Quiz Detail Screen (Fixed function signature)
        composable(
            "quiz_detail/{quizName}",
            arguments = listOf(navArgument("quizName") { type = NavType.StringType })
        ) { backStackEntry ->
            val quizName = backStackEntry.arguments?.getString("quizName") ?: ""
            QuizDetailScreen(
                quizName = quizName,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onQuizComplete = { score ->
                    viewModel.addQuizResult(quizName, score, 10) // Assuming 10 max score
                }
            )
        }

        // Quiz History Screen (Fixed function signature)
        composable("quiz_history") {
            QuizHistoryScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Task Screen
        composable("tasks") {
            TaskScreen(
                viewModel = viewModel
            )
        }

        // Quiz Screens (Additional quiz screens)
        composable("quiz_screens") {
            QuizScreensMain(
                viewModel = viewModel,
                onNavigateToQuiz = { quizName ->
                    navController.navigate("quiz_detail/$quizName")
                },
                onNavigateToHistory = {
                    navController.navigate("quiz_history")
                }
            )
        }
    }
}

// Additional Quiz Screens container
@Composable
fun QuizScreensMain(
    viewModel: MainViewModel,
    onNavigateToQuiz: (String) -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    // This can be expanded to include quiz selection, categories, etc.
    MainScreen(
        viewModel = viewModel,
        onNavigateToQuiz = onNavigateToQuiz,
        modifier = modifier
    )
}