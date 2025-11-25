package com.example.somashare.data.repository

import com.example.somashare.data.model.User
import com.example.somashare.data.model.UserRole
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun getUserById(userId: String): Flow<User?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                trySend(user)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("users").child(userId).addValueEventListener(listener)
        awaitClose { database.child("users").child(userId).removeEventListener(listener) }
    }

    suspend fun updateUser(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            database.child("users").child(userId).updateChildren(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllUsersByRole(role: UserRole): Flow<List<User>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (child in snapshot.children) {
                    val user = child.getValue(User::class.java)
                    if (user != null && user.role == role) {
                        users.add(user)
                    }
                }
                trySend(users)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("users").addValueEventListener(listener)
        awaitClose { database.child("users").removeEventListener(listener) }
    }

    suspend fun deactivateUser(userId: String): Result<Unit> {
        return updateUser(userId, mapOf("isActive" to false))
    }

    suspend fun activateUser(userId: String): Result<Unit> {
        return updateUser(userId, mapOf("isActive" to true))
    }
}