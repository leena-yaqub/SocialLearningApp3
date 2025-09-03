package com.example.sociallearningapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserPresenceManager {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    suspend fun setUserOnline() {
        val uid = auth.currentUser?.uid ?: return
        database.reference.child("users").child(uid).child("online").setValue(true)
    }

    suspend fun setUserOffline() {
        val uid = auth.currentUser?.uid ?: return
        database.reference.child("users").child(uid).child("online").setValue(false)
    }
}