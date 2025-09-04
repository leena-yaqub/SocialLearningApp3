package com.example.sociallearningapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sociallearningapp.MainActivity
import com.example.sociallearningapp.data.PreferencesManager
import com.example.sociallearningapp.data.repository.ChatRepository
import com.example.sociallearningapp.data.repository.QuizRepository
import com.example.sociallearningapp.data.repository.TaskRepository
import com.example.sociallearningapp.screens.*
import com.example.sociallearningapp.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val mainViewModel: MainViewModel = viewModel()

    val quizRepository = QuizRepository()
    val taskRepository = TaskRepository()
    val chatRepository = ChatRepository()

    val quizViewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(quizRepository)
    )
    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(taskRepository)
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(quizRepository, taskRepository)
    )
    val chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(chatRepository)
    )

    val adsManager = (LocalContext.current as MainActivity).adsManager

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(onNavigate = {
                val isFirstLaunch by preferencesManager.isFirstLaunch.collectAsState(initial = true)
                LaunchedEffect(isFirstLaunch) {
                    if (isFirstLaunch) {
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser != null) {
                            navController.navigate("main") {
                                popUpTo("splash") { inclusive = true }
                            }
                        } else {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                }
            })
        }
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                preferencesManager = preferencesManager,
                adsManager = adsManager
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = mainViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = mainViewModel,
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(
                quizViewModel = quizViewModel,
                taskViewModel = taskViewModel,
                chatViewModel = chatViewModel,
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToQuiz = { quizId ->
                    navController.navigate("quiz_detail/$quizId")
                }
            )
        }
        composable("profile") {
            ProfileScreen(viewModel = profileViewModel)
        }
        composable("quiz_list") {
            QuizListScreen(
                viewModel = quizViewModel,
                onNavigateToQuiz = { quizId ->
                    navController.navigate("quiz_detail/$quizId")
                }
            )
        }
        composable(
            "quiz_detail/{quizId}",
            arguments = listOf(navArgument("quizId") { type = NavType.LongType })
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getLong("quizId") ?: 0L
            QuizDetailScreen(
                quizViewModel = quizViewModel,
                quizId = quizId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onQuizComplete = {
                    navController.navigate("quiz_result") {
                        popUpTo("quiz_detail/{quizId}") { inclusive = true }
                    }
                }
            )
        }
        composable("quiz_result") {
            QuizResultScreen(
                viewModel = quizViewModel,
                onNavigateToHistory = {
                    navController.navigate("quiz_history") {
                        popUpTo("quiz_result") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("main") {
                        popUpTo("quiz_result") { inclusive = true }
                    }
                }
            )
        }
        composable("quiz_history") {
            QuizHistoryScreen(
                viewModel = quizViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("tasks") {
            TaskScreen(
                viewModel = taskViewModel
            )
        }
    }
}