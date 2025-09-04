package com.example.sociallearningapp.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sociallearningapp.MainActivity
import com.example.sociallearningapp.ads.AdsManager
import com.example.sociallearningapp.data.PreferencesManager
import com.example.sociallearningapp.screens.LoginScreen
import com.example.sociallearningapp.screens.MainScreen
import com.example.sociallearningapp.screens.OnboardingScreen
import com.example.sociallearningapp.screens.RegisterScreen
import com.example.sociallearningapp.screens.SplashScreen
import com.example.sociallearningapp.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    adsManager: AdsManager,
    modifier: Modifier = Modifier
) {
    val mainViewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen {
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
            }
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
            // This will be a placeholder for now
            MainScreen(viewModel = mainViewModel, onNavigateToQuiz = {})
        }
    }
}