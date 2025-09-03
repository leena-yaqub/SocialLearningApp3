// File: app/src/main/java/com/example/sociallearningapp/MainActivity.kt
package com.example.sociallearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.sociallearningapp.data.PreferencesManager
import com.example.sociallearningapp.navigation.AppNavigation
import com.example.sociallearningapp.ui.theme.SocialLearningAppTheme
import com.example.sociallearningapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialLearningAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val mainViewModel: MainViewModel = viewModel()
                    val preferencesManager = PreferencesManager(this@MainActivity)

                    AppNavigation(
                        navController = navController,
                        viewModel = mainViewModel,
                        preferencesManager = preferencesManager
                    )
                }
            }
        }
    }
}