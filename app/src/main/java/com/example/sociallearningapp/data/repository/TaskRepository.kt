package com.example.sociallearningapp.data.repository

import com.example.sociallearningapp.data.dao.TaskDao
import com.example.sociallearningapp.data.dao.QuizHistoryDao
import com.example.sociallearningapp.data.model.Task
import com.example.sociallearningapp.data.model.QuizHistory
import com.example.sociallearningapp.data.model.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val quizHistoryDao: QuizHistoryDao
) {

    suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }

    suspend fun getTaskById(id: String): Task? {
        return taskDao.getTaskById(id)
    }

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun getCompletedTasksCount(): Int {
        return taskDao.getCompletedTasksCount()
    }

    // Quiz History methods
    suspend fun getAllQuizHistory(): List<QuizHistory> {
        return quizHistoryDao.getAllQuizHistory()
    }

    suspend fun insertQuizHistory(quizHistory: QuizHistory): Long {
        return quizHistoryDao.insertQuizHistory(quizHistory)
    }

    suspend fun getQuizHistoryCount(): Int {
        return quizHistoryDao.getQuizHistoryCount()
    }

    // Sample data methods for development (using epoch millis)
    suspend fun insertSampleTasks() {
        val now = System.currentTimeMillis()
        val sampleTasks = listOf(
            Task(
                title = "Complete Math Assignment",
                description = "Finish chapter 5 exercises on algebra",
                priority = Priority.HIGH,
                createdAt = now - 0L,
                isCompleted = false
            ),
            Task(
                title = "Read Science Article",
                description = "Read about quantum physics and take notes",
                priority = Priority.MEDIUM,
                createdAt = now - 86_400_000L,
                isCompleted = true
            ),
            Task(
                title = "Practice Vocabulary",
                description = "Learn 20 new vocabulary words for English class",
                priority = Priority.LOW,
                createdAt = now - 172_800_000L,
                isCompleted = false
            ),
            Task(
                title = "History Essay",
                description = "Write 500-word essay on World War II",
                priority = Priority.HIGH,
                createdAt = now - 259_200_000L,
                isCompleted = true
            )
        )

        sampleTasks.forEach { task ->
            insertTask(task)
        }
    }

    suspend fun insertSampleQuizHistory() {
        val now = System.currentTimeMillis()
        val sampleHistory = listOf(
            QuizHistory(
                quizId = 1,
                quizName = "Math Basics Quiz",
                score = 8,
                maxScore = 10,
                completedAt = now
            ),
            QuizHistory(
                quizId = 2,
                quizName = "Science Knowledge Test",
                score = 7,
                maxScore = 10,
                completedAt = now - 86_400_000L
            ),
            QuizHistory(
                quizId = 3,
                quizName = "History Challenge",
                score = 9,
                maxScore = 10,
                completedAt = now - 172_800_000L
            ),
            QuizHistory(
                quizId = 4,
                quizName = "English Grammar",
                score = 6,
                maxScore = 10,
                completedAt = now - 259_200_000L
            )
        )

        sampleHistory.forEach { history ->
            insertQuizHistory(history)
        }
    }

    // Flow-based methods for reactive UI
    fun getTasksFlow(): Flow<List<Task>> = flow {
        emit(getAllTasks())
    }

    fun getQuizHistoryFlow(): Flow<List<QuizHistory>> = flow {
        emit(getAllQuizHistory())
    }

    fun getCompletedTasksCountFlow(): Flow<Int> = flow {
        emit(getCompletedTasksCount())
    }
}
