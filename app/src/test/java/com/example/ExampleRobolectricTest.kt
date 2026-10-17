package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.WorkoutDatabase
import com.example.data.model.ExerciseProgressEntity
import com.example.data.model.WorkoutDataSource
import com.example.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: WorkoutDatabase
    private lateinit var repository: WorkoutRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, WorkoutDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = WorkoutRepository(database.workoutDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context matches app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Fahur Workout", appName)
    }

    @Test
    fun `workout data source contains expected days`() {
        assertEquals("Fahur Assessoria", WorkoutDataSource.TREINADOR)
        assertEquals("Fahur", WorkoutDataSource.ALUNO)
        assertEquals(6, WorkoutDataSource.diasTreino.size)

        val coringa = WorkoutDataSource.diasTreino.first { it.diaId == "coringa" }
        assertTrue(coringa.isCoringa)
        assertEquals(5, coringa.exercicios.size)
    }

    @Test
    fun `repository saves and updates set weights and completion`() = runBlocking {
        repository.saveSetWeight("cor_1", 1, "24.5")
        repository.saveSetCompletion("cor_1", 1, true)
        repository.saveCustomNote("cor_1", "Pegada mista")

        val progress = repository.allProgressMap.first()
        val cor1Progress = progress["cor_1"]

        org.junit.Assert.assertNotNull(cor1Progress)
        assertEquals("24.5", cor1Progress!!.getWeightsMap()[1])
        assertEquals(true, cor1Progress.getCompletedSetsMap()[1])
        assertEquals("Pegada mista", cor1Progress.customNote)

        // Test weekly reset: unchecks completed sets while preserving weights and notes
        repository.resetWeekChecks()
        val afterReset = repository.allProgressMap.first()["cor_1"]!!
        assertEquals("24.5", afterReset.getWeightsMap()[1])
        assertEquals("Pegada mista", afterReset.customNote)
        assertFalse(afterReset.getCompletedSetsMap().containsKey(1))
    }
}
