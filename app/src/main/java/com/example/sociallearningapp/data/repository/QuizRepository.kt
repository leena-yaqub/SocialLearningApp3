package com.example.sociallearningapp.repository

import com.example.sociallearningapp.data.model.Quiz
import com.example.sociallearningapp.data.model.QuizResult
import com.example.sociallearningapp.data.dao.QuizDao
import com.example.sociallearningapp.data.dao.QuizResultDao
import com.example.sociallearningapp.data.SampleQuizData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizDao: QuizDao,
    private val quizResultDao: QuizResultDao
) {

    fun getAllQuizzes(): Flow<List<Quiz>> = quizDao.getAllQuizzes()

    suspend fun getQuizById(id: Long): Quiz? = quizDao.getQuizById(id)

    suspend fun insertQuiz(quiz: Quiz): Long = quizDao.insertQuiz(quiz)

    suspend fun insertSampleQuizzes() {
        SampleQuizData.getAllSampleQuizzes().forEach { quiz ->
            insertQuiz(quiz)
        }
    }

    fun getUserQuizResults(userId: String): Flow<List<QuizResult>> =
        quizResultDao.getUserQuizResults(userId)

    suspend fun saveQuizResult(result: QuizResult): Long =
        quizResultDao.insertQuizResult(result)

    suspend fun getQuizCountForUser(userId: String): Int =
        quizResultDao.getQuizCountForUser(userId)

    suspend fun getAverageScoreForUser(userId: String): Double =
        quizResultDao.getAverageScoreForUser(userId) ?: 0.0
}
