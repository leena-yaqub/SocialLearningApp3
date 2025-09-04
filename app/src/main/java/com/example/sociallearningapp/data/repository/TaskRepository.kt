package com.example.sociallearningapp.data.repository

import com.example.sociallearningapp.data.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val tasksRef: DatabaseReference?
        get() = auth.currentUser?.uid?.let { database.getReference("tasks").child(it) }

    fun getTasksFlow(): Flow<List<Task>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = snapshot.children.mapNotNull { it.getValue(Task::class.java) }
                trySend(tasks.sortedByDescending { it.createdAt })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = tasksRef
        ref?.addValueEventListener(listener)
        awaitClose { ref?.removeEventListener(listener) }
    }

    suspend fun addTask(task: Task) {
        val user = auth.currentUser ?: return
        val taskWithUser = task.copy(userId = user.uid)
        tasksRef?.child(task.id)?.setValue(taskWithUser)?.await()
    }

    suspend fun updateTask(task: Task) {
        tasksRef?.child(task.id)?.setValue(task)?.await()
    }

    suspend fun deleteTask(taskId: String) {
        tasksRef?.child(taskId)?.removeValue()?.await()
    }
}
