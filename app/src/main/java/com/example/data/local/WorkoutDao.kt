package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ExerciseProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM exercise_progress")
    fun getAllProgress(): Flow<List<ExerciseProgressEntity>>

    @Query("SELECT * FROM exercise_progress WHERE exerciseId = :id LIMIT 1")
    suspend fun getProgressById(id: String): ExerciseProgressEntity?

    @Query("SELECT * FROM exercise_progress WHERE exerciseId = :id LIMIT 1")
    fun observeProgressById(id: String): Flow<ExerciseProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: ExerciseProgressEntity)

    @Query("UPDATE exercise_progress SET completedSetsJson = '{}'")
    suspend fun resetAllCompletedSets()

    @Query("DELETE FROM exercise_progress")
    suspend fun clearAll()
}
