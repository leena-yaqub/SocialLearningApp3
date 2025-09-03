package com.example.sociallearningapp.data.dao

import androidx.room.*
import com.example.sociallearningapp.data.model.QuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY timestamp DESC")
    fun getUserQuizResults(userId: String): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results WHERE userId = :userId AND quizId = :quizId ORDER BY timestamp DESC")
    suspend fun getQuizResultsForUserAndQuiz(userId: String, quizId: Long): List<QuizResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult): Long

    @Query("SELECT COUNT(*) FROM quiz_results WHERE userId = :userId")
    suspend fun getQuizCountForUser(userId: String): Int

    @Query("SELECT AVG(score * 100.0 / totalQuestions) FROM quiz_results WHERE userId = :userId")
    suspend fun getAverageScoreForUser(userId: String): Double?

    @Delete
    suspend fun deleteQuizResult(result: QuizResult)

    @Query("DELETE FROM quiz_results WHERE userId = :userId")
    suspend fun deleteAllResultsForUser(userId: String)
}
