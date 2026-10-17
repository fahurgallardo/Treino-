package com.example.ui.timer

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RestTimerState(
    val isActive: Boolean = false,
    val isPaused: Boolean = false,
    val totalSeconds: Int = 0,
    val remainingSeconds: Int = 0,
    val isFinished: Boolean = false,
    val exerciseName: String = ""
)

class RestTimerManager(private val context: Context, private val scope: CoroutineScope) {

    private val _timerState = MutableStateFlow(RestTimerState())
    val timerState: StateFlow<RestTimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90)
        } catch (e: Exception) {
            // ToneGenerator might fail if device has no audio routing
        }
    }

    fun startTimer(seconds: Int, exerciseName: String = "") {
        timerJob?.cancel()
        _timerState.value = RestTimerState(
            isActive = true,
            isPaused = false,
            totalSeconds = seconds,
            remainingSeconds = seconds,
            isFinished = false,
            exerciseName = exerciseName
        )

        timerJob = scope.launch(Dispatchers.Default) {
            while (_timerState.value.remainingSeconds > 0) {
                delay(1000L)
                if (!_timerState.value.isPaused) {
                    _timerState.update { current ->
                        val next = current.remainingSeconds - 1
                        if (next <= 0) {
                            current.copy(remainingSeconds = 0, isFinished = true)
                        } else {
                            current.copy(remainingSeconds = next)
                        }
                    }
                }
            }

            // Finished! Trigger industrial sound and vibration
            triggerCompletionAlert()
        }
    }

    fun pauseOrResume() {
        _timerState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun addSeconds(delta: Int) {
        _timerState.update { current ->
            val newRemaining = (current.remainingSeconds + delta).coerceAtLeast(1)
            val newTotal = maxOf(current.totalSeconds, newRemaining)
            current.copy(remainingSeconds = newRemaining, totalSeconds = newTotal, isFinished = false)
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _timerState.value = RestTimerState()
    }

    private fun triggerCompletionAlert() {
        // Sound beep
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 400)
        } catch (e: Exception) {
            // ignore
        }

        // Vibration
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createWaveform(
                            longArrayOf(0, 200, 100, 200),
                            -1
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(longArrayOf(0, 200, 100, 200), -1)
                }
            }
        } catch (e: Exception) {
            // ignore vibration error
        }
    }

    fun release() {
        timerJob?.cancel()
        toneGenerator?.release()
        toneGenerator = null
    }
}
