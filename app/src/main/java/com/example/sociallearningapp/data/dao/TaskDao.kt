package com.example.sociallearningapp.data.dao

import androidx.room.*
import com.example.sociallearningapp.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTasksForUser(userId: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = :isCompleted ORDER BY createdAt DESC")
    fun getTasksForUserByStatus(userId: String, isCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE userId = :userId")
    suspend fun deleteAllTasksForUser(userId: String)

    @Query("SELECT COUNT(*) FROM tasks WHERE userId = :userId AND isCompleted = :isCompleted")
    suspend fun getTaskCountForUser(userId: String, isCompleted: Boolean): Int
}
