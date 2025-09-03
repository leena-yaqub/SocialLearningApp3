package com.example.sociallearningapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "avatar_0",
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis()
)