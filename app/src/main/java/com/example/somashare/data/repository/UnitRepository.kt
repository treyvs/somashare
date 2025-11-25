package com.example.somashare.data.repository

import com.example.somashare.data.model.Unit
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UnitRepository {
    private val database = FirebaseDatabase.getInstance().reference

    suspend fun createUnit(unit: Unit): Result<String> {
        return try {
            val unitId = database.child("units").push().key
                ?: throw Exception("Failed to generate unit ID")
            val unitWithId = unit.copy(unitId = unitId)
            database.child("units").child(unitId).setValue(unitWithId.toMap()).await()
            Result.success(unitId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllUnits(): Flow<List<Unit>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val units = mutableListOf<Unit>()
                for (child in snapshot.children) {
                    child.getValue(Unit::class.java)?.let { unit ->
                        if (unit.isActive) units.add(unit)
                    }
                }
                trySend(units.sortedBy { it.unitCode })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("units").addValueEventListener(listener)
        awaitClose { database.child("units").removeEventListener(listener) }
    }

    fun getUnitsByYearAndSemester(year: Int, semester: Int): Flow<List<Unit>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val units = mutableListOf<Unit>()
                for (child in snapshot.children) {
                    child.getValue(Unit::class.java)?.let { unit ->
                        if (unit.isActive && unit.yearOfStudy == year &&
                            unit.semesterOfStudy == semester) {
                            units.add(unit)
                        }
                    }
                }
                trySend(units.sortedBy { it.unitCode })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.child("units").addValueEventListener(listener)
        awaitClose { database.child("units").removeEventListener(listener) }
    }

    suspend fun updateUnit(unitId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            database.child("units").child(unitId).updateChildren(updates).await()
            Result.success(kotlin.Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUnit(unitId: String): Result<Unit> {
        return try {
            database.child("units").child(unitId)
                .child("isActive").setValue(false).await()
            Result.success(kotlin.Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
