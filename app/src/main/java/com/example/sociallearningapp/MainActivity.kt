package com.example.sociallearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.sociallearningapp.ads.AdsManager
import com.example.sociallearningapp.data.PreferencesManager
import com.example.sociallearningapp.navigation.AppNavigation
import com.example.sociallearningapp.ui.theme.SocialLearningAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var adsManager: AdsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adsManager = AdsManager(this)
        adsManager.loadInterstitialAd()

        setContent {
            SocialLearningAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val preferencesManager = PreferencesManager(this@MainActivity)

                    AppNavigation(
                        navController = navController,
                        preferencesManager = preferencesManager,
                        adsManager = adsManager
                    )
                }
            }
        }
    }
}