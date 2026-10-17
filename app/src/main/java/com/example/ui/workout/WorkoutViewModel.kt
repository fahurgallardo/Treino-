package com.example.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.WorkoutDatabase
import com.example.data.model.ExerciseProgressEntity
import com.example.data.model.WorkoutDataSource
import com.example.data.model.WorkoutDay
import com.example.data.repository.WorkoutRepository
import com.example.ui.timer.RestTimerManager
import com.example.ui.timer.RestTimerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorkoutRepository
    val timerManager: RestTimerManager

    init {
        val database = WorkoutDatabase.getDatabase(application)
        repository = WorkoutRepository(database.workoutDao())
        timerManager = RestTimerManager(application.applicationContext, viewModelScope)
    }

    val workoutDays: List<WorkoutDay> = WorkoutDataSource.diasTreino

    private val _selectedDayId = MutableStateFlow<String?>(null)
    val selectedDayId: StateFlow<String?> = _selectedDayId.asStateFlow()

    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog.asStateFlow()

    val progressMap: StateFlow<Map<String, ExerciseProgressEntity>> =
        repository.allProgressMap.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    val timerState: StateFlow<RestTimerState> = timerManager.timerState

    fun selectDay(dayId: String) {
        _selectedDayId.value = dayId
    }

    fun clearSelectedDay() {
        _selectedDayId.value = null
    }

    fun updateWeight(exerciseId: String, setNumber: Int, weight: String) {
        viewModelScope.launch {
            repository.saveSetWeight(exerciseId, setNumber, weight)
        }
    }

    fun toggleSetCompletion(exerciseId: String, setNumber: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.saveSetCompletion(exerciseId, setNumber, isCompleted)
        }
    }

    fun updateNote(exerciseId: String, note: String) {
        viewModelScope.launch {
            repository.saveCustomNote(exerciseId, note)
        }
    }

    fun requestResetDialog() {
        _showResetDialog.value = true
    }

    fun dismissResetDialog() {
        _showResetDialog.value = false
    }

    fun confirmResetWeek() {
        viewModelScope.launch {
            repository.resetWeekChecks()
            _showResetDialog.value = false
        }
    }

    fun startTimer(seconds: Int, exerciseName: String) {
        timerManager.startTimer(seconds, exerciseName)
    }

    fun stopTimer() {
        timerManager.stopTimer()
    }

    fun pauseResumeTimer() {
        timerManager.pauseOrResume()
    }

    fun addTimerSeconds(seconds: Int) {
        timerManager.addSeconds(seconds)
    }

    override fun onCleared() {
        super.onCleared()
        timerManager.release()
    }
}
