package com.example.sociallearningapp.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sociallearningapp.ads.AdsManager
import com.example.sociallearningapp.ads.BannerAd
import com.example.sociallearningapp.data.PreferencesManager
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    preferencesManager: PreferencesManager,
    adsManager: AdsManager
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPage(page = page)
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        if (pagerState.currentPage == 2) {
            Button(
                onClick = {
                    scope.launch {
                        preferencesManager.setFirstLaunchComplete()
                        adsManager.showInterstitialAd(context as Activity) {
                            onFinish()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Finish")
            }
        } else {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun OnboardingPage(page: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val title: String
        val description: String

        when (page) {
            0 -> {
                title = "Welcome to the Social Learning App"
                description = "Your new favorite app for learning and collaborating with others."
            }
            1 -> {
                title = "Features Overview"
                description = "Take quizzes, manage your tasks, and chat with your study group in real-time."
            }
            else -> {
                title = "Privacy & Terms"
                description = "By using this app, you agree to our terms and conditions."
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        BannerAd()
    }
}