package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.ChatMessage
import com.example.sociallearningapp.data.model.User
import com.example.sociallearningapp.data.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val messages: StateFlow<List<ChatMessage>> = repository.getMessagesFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val onlineUsers: StateFlow<List<User>> = repository.getOnlineUsersFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        repository.setUserOnline()
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            val user = auth.currentUser ?: return@launch
            val message = ChatMessage(
                senderId = user.uid,
                senderName = user.displayName ?: "Anonymous",
                message = messageText,
                timestamp = System.currentTimeMillis()
            )
            repository.sendMessage(message)
        }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun onCleared() {
        super.onCleared()
        repository.setUserOffline()
    }
}