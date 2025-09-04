package com.example.sociallearningapp.data.repository

import com.example.sociallearningapp.data.SampleQuizData
import com.example.sociallearningapp.data.model.Quiz
import com.example.sociallearningapp.data.model.QuizQuestion
import com.example.sociallearningapp.data.model.QuizResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun getAllQuizzes(): Flow<List<Quiz>> = flow {
        // For now, we use hardcoded sample data.
        // In a real app, this might fetch from Firebase or a local DB.
        emit(SampleQuizData.getQuizzes())
    }

    suspend fun getQuizById(quizId: Long): Quiz? {
        return SampleQuizData.getQuizzes().find { it.id == quizId }
    }

    fun getQuestionsForQuiz(quizId: Long): List<QuizQuestion> {
        return when (quizId) {
            1L -> SampleQuizData.getMathQuestions()
            2L -> SampleQuizData.getScienceQuestions()
            3L -> SampleQuizData.getHistoryQuestions()
            else -> SampleQuizData.getGeneralKnowledgeQuestions()
        }
    }

    suspend fun saveQuizResult(result: QuizResult) {
        auth.currentUser?.uid?.let { userId ->
            val resultId = database.getReference("quiz_results").child(userId).push().key
            if (resultId != null) {
                // We don't need to save the push key as the ID in this case,
                // as Firebase keys are unique.
                database.getReference("quiz_results").child(userId).child(resultId).setValue(result).await()
            }
        }
    }

    fun getQuizHistory(userId: String): Flow<List<QuizResult>> = flow {
        val quizResults = mutableListOf<QuizResult>()
        database.getReference("quiz_results").child(userId).get().await().children.forEach { dataSnapshot ->
            dataSnapshot.getValue(QuizResult::class.java)?.let { quizResult ->
                quizResults.add(quizResult)
            }
        }
        emit(quizResults.sortedByDescending { it.timestamp })
    }
}
