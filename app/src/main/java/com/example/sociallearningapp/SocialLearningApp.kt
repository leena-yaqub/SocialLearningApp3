package com.example.sociallearningapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp

class SocialLearningApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus ->
            // Handle initialization completion if needed
        }
    }
}