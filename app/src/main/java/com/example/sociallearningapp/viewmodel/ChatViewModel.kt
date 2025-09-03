// File: app/src/main/java/com/example/sociallearningapp/viewmodel/ChatViewModel.kt
package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.ChatMessage
import com.example.sociallearningapp.data.model.User
import com.example.sociallearningapp.data.repository.ChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _onlineUsers = MutableStateFlow<List<User>>(emptyList())
    val onlineUsers: StateFlow<List<User>> = _onlineUsers.asStateFlow()

    init {
        loadMessages()
        loadOnlineUsers()
        setUserOnline()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            try {
                repository.getMessagesFlow().collect { messageList ->
                    _messages.value = messageList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load messages: ${e.message}"
            }
        }
    }

    private fun loadOnlineUsers() {
        viewModelScope.launch {
            try {
                repository.getOnlineUsersFlow().collect { users ->
                    _onlineUsers.value = users
                }
            } catch (e: Exception) {
                // Handle error silently for online users as it's not critical
            }
        }
    }

    private fun setUserOnline() {
        viewModelScope.launch {
            repository.setUserOnline()
        }
    }

    fun sendMessage(message: String) {
        if (message.trim().isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.sendMessage(message.trim())
                if (!success) {
                    _errorMessage.value = "Failed to send message"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error sending message: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCurrentUserId(): String? = repository.getCurrentUserId()

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        // Set user offline when ViewModel is destroyed
        viewModelScope.launch {
            repository.setUserOffline()
        }
    }
}