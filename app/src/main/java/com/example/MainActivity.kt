package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.IndustrialBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.workout.HomeScreen
import com.example.ui.workout.RestTimerOverlay
import com.example.ui.workout.WorkoutDetailScreen
import com.example.ui.workout.WorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                WorkoutAppRoot()
            }
        }
    }
}

@Composable
fun WorkoutAppRoot(
    viewModel: WorkoutViewModel = viewModel()
) {
    val selectedDayId by viewModel.selectedDayId.collectAsStateWithLifecycle()
    val progressMap by viewModel.progressMap.collectAsStateWithLifecycle()
    val showResetDialog by viewModel.showResetDialog.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()

    val currentDay = viewModel.workoutDays.firstOrNull { it.diaId == selectedDayId }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(IndustrialBlack),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = IndustrialBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (currentDay == null) {
                HomeScreen(
                    workoutDays = viewModel.workoutDays,
                    progressMap = progressMap,
                    onDaySelected = { dayId -> viewModel.selectDay(dayId) },
                    showResetDialog = showResetDialog,
                    onRequestResetDialog = { viewModel.requestResetDialog() },
                    onConfirmReset = { viewModel.confirmResetWeek() },
                    onDismissResetDialog = { viewModel.dismissResetDialog() }
                )
            } else {
                WorkoutDetailScreen(
                    day = currentDay,
                    progressMap = progressMap,
                    onBack = { viewModel.clearSelectedDay() },
                    onWeightChanged = { exerciseId, setNum, weight ->
                        viewModel.updateWeight(exerciseId, setNum, weight)
                    },
                    onSetCompletionToggled = { exerciseId, setNum, completed ->
                        viewModel.toggleSetCompletion(exerciseId, setNum, completed)
                    },
                    onNoteChanged = { exerciseId, note ->
                        viewModel.updateNote(exerciseId, note)
                    },
                    onStartRestTimer = { seconds, exerciseName ->
                        viewModel.startTimer(seconds, exerciseName)
                    }
                )
            }

            // Floating rest timer overlay
            RestTimerOverlay(
                timerState = timerState,
                onPauseResume = { viewModel.pauseResumeTimer() },
                onAddSeconds = { delta -> viewModel.addTimerSeconds(delta) },
                onClose = { viewModel.stopTimer() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}
