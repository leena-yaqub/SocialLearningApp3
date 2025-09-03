package com.example.sociallearningapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase

class SocialLearningApplication : Application() {

    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var firestore: FirebaseFirestore
        lateinit var database: FirebaseDatabase
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        database = FirebaseDatabase.getInstance()
    }
}
