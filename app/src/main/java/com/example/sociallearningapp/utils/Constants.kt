package com.example.sociallearningapp.utils

object Constants {
    // AdMob Test Ad Unit IDs
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // Firebase Firestore collection names
    const val USERS_COLLECTION = "users"
    const val QUIZ_RESULTS_COLLECTION = "quiz_results"
    const val TASKS_COLLECTION = "tasks"
    const val MESSAGES_COLLECTION = "messages"
    const val QUIZZES_COLLECTION = "quizzes"

    // Quiz Settings
    const val DEFAULT_QUESTION_TIME = 10
    const val QUESTIONS_PER_QUIZ = 5

    // Navigation Routes
    const val LOGIN_SCREEN = "login"
    const val REGISTER_SCREEN = "register"
    const val MAIN_SCREEN = "main"
    const val CHAT_SCREEN = "chat"
    const val QUIZ_SCREEN = "quiz"
    const val TASK_SCREEN = "task"
    const val PROFILE_SCREEN = "profile"
    const val SPLASH_SCREEN = "splash"
    const val ONBOARDING_SCREEN = "onboarding"
}