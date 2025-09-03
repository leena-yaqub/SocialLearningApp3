// File: data/model/Converters.kt
package com.example.sociallearningapp.data.model

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return try {
            Priority.valueOf(priority)
        } catch (e: IllegalArgumentException) {
            Priority.MEDIUM // default fallback
        }
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        return if (value.isEmpty()) {
            emptyList()
        } else {
            value.split(",").map { it.trim() }
        }
    }

    @TypeConverter
    fun fromListString(list: List<String>): String {
        return list.joinToString(",")
    }

    // Simple integer list converter
    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toIntList(data: String): List<Int> {
        return if (data.isEmpty()) {
            emptyList()
        } else {
            data.split(",").mapNotNull { it.trim().toIntOrNull() }
        }
    }
}