package com.example.sociallearningapp.data.dao

import androidx.room.*
import com.example.sociallearningapp.data.model.Quiz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM quizzes ORDER BY createdAt DESC")
    fun getAllQuizzes(): Flow<List<Quiz>>

    @Query("SELECT * FROM quizzes WHERE id = :id")
    suspend fun getQuizById(id: Long): Quiz?

    @Query("SELECT * FROM quizzes WHERE category = :category ORDER BY createdAt DESC")
    suspend fun getQuizzesByCategory(category: String): List<Quiz>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz): Long

    @Update
    suspend fun updateQuiz(quiz: Quiz)

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("DELETE FROM quizzes")
    suspend fun deleteAllQuizzes()
}
