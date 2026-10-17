package com.example.data.repository

import com.example.data.local.WorkoutDao
import com.example.data.model.ExerciseProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepository(private val dao: WorkoutDao) {

    val allProgressMap: Flow<Map<String, ExerciseProgressEntity>> =
        dao.getAllProgress().map { list ->
            list.associateBy { it.exerciseId }
        }

    suspend fun saveSetWeight(exerciseId: String, setNumber: Int, weight: String) {
        val current = dao.getProgressById(exerciseId) ?: ExerciseProgressEntity(exerciseId = exerciseId)
        val currentWeights = current.getWeightsMap().toMutableMap()
        if (weight.isBlank()) {
            currentWeights.remove(setNumber)
        } else {
            currentWeights[setNumber] = weight
        }
        val updated = current.copy(
            weightsJson = ExerciseProgressEntity.serializeWeights(currentWeights),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertOrUpdate(updated)
    }

    suspend fun saveSetCompletion(exerciseId: String, setNumber: Int, isCompleted: Boolean) {
        val current = dao.getProgressById(exerciseId) ?: ExerciseProgressEntity(exerciseId = exerciseId)
        val currentSets = current.getCompletedSetsMap().toMutableMap()
        if (!isCompleted) {
            currentSets.remove(setNumber)
        } else {
            currentSets[setNumber] = true
        }
        val updated = current.copy(
            completedSetsJson = ExerciseProgressEntity.serializeCompletedSets(currentSets),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertOrUpdate(updated)
    }

    suspend fun saveCustomNote(exerciseId: String, note: String) {
        val current = dao.getProgressById(exerciseId) ?: ExerciseProgressEntity(exerciseId = exerciseId)
        val updated = current.copy(
            customNote = note,
            updatedAt = System.currentTimeMillis()
        )
        dao.insertOrUpdate(updated)
    }

    suspend fun resetWeekChecks() {
        dao.resetAllCompletedSets()
    }
}
