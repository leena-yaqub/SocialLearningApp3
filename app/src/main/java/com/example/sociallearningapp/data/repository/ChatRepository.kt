// File: app/src/main/java/com/example/sociallearningapp/data/repository/ChatRepository.kt
package com.example.sociallearningapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.sociallearningapp.data.model.ChatMessage
import com.example.sociallearningapp.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose

class ChatRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // Real-time chat messages from Firebase Realtime Database
    fun getMessagesFlow(): Flow<List<ChatMessage>> = callbackFlow {
        val messagesRef = database.child("messages")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<ChatMessage>()
                snapshot.children.forEach { child ->
                    child.getValue(ChatMessage::class.java)?.let { message ->
                        messages.add(message.copy(id = child.key ?: ""))
                    }
                }
                // Sort by timestamp
                messages.sortBy { it.timestamp }
                trySend(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        messagesRef.addValueEventListener(listener)
        awaitClose { messagesRef.removeEventListener(listener) }
    }

    // Send a new message to Firebase Realtime Database
    suspend fun sendMessage(message: String): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val userName = auth.currentUser?.displayName ?: "Anonymous User"

        val chatMessage = ChatMessage(
            message = message,
            senderName = userName,
            senderId = uid,
            timestamp = System.currentTimeMillis()
        )

        return try {
            val messageRef = database.child("messages").push()
            messageRef.setValue(chatMessage).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Get the currently logged-in user's ID
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Real-time online users tracking
    fun getOnlineUsersFlow(): Flow<List<User>> = callbackFlow {
        val onlineUsersRef = database.child("onlineUsers")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                snapshot.children.forEach { child ->
                    child.getValue(User::class.java)?.let { user ->
                        users.add(user)
                    }
                }
                trySend(users)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        onlineUsersRef.addValueEventListener(listener)
        awaitClose { onlineUsersRef.removeEventListener(listener) }
    }

    // Set user online status
    suspend fun setUserOnline() {
        val uid = auth.currentUser?.uid ?: return
        val userName = auth.currentUser?.displayName ?: "Anonymous User"
        val userEmail = auth.currentUser?.email ?: ""

        val user = User(
            id = uid,
            name = userName,
            email = userEmail,
            isOnline = true,
            lastSeen = System.currentTimeMillis()
        )

        try {
            database.child("onlineUsers").child(uid).setValue(user).await()
            // Set up automatic cleanup when user disconnects
            database.child("onlineUsers").child(uid).onDisconnect().removeValue()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Set user offline status
    suspend fun setUserOffline() {
        val uid = auth.currentUser?.uid ?: return
        try {
            database.child("onlineUsers").child(uid).removeValue().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}