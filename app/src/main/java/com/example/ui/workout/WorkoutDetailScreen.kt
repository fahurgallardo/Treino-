package com.example.ui.workout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExerciseProgressEntity
import com.example.data.model.WorkoutDay
import com.example.ui.theme.IndustrialBlack
import com.example.ui.theme.IndustrialBorder
import com.example.ui.theme.IndustrialOrange
import com.example.ui.theme.IndustrialSuccess
import com.example.ui.theme.IndustrialTextPrimary
import com.example.ui.theme.IndustrialTextSecondary

@Composable
fun WorkoutDetailScreen(
    day: WorkoutDay,
    progressMap: Map<String, ExerciseProgressEntity>,
    onBack: () -> Unit,
    onWeightChanged: (exerciseId: String, setNum: Int, weight: String) -> Unit,
    onSetCompletionToggled: (exerciseId: String, setNum: Int, completed: Boolean) -> Unit,
    onNoteChanged: (exerciseId: String, note: String) -> Unit,
    onStartRestTimer: (seconds: Int, exerciseName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }

    val totalSets = day.exercicios.sumOf { it.series }
    val completedSets = day.exercicios.sumOf { ex ->
        progressMap[ex.id]?.getCompletedSetsMap()?.values?.count { it } ?: 0
    }
    val progressFraction = if (totalSets > 0) completedSets.toFloat() / totalSets.toFloat() else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialBlack)
            .testTag("workout_detail_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Back Button & Header
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("back_to_home_button"),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF1A1A1A),
                        contentColor = IndustrialTextPrimary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndustrialBorder))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar ao Início",
                        tint = IndustrialTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voltar ao Início",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Day Title and Focus Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E1E1E))
                        .border(1.dp, IndustrialBorder, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (day.isCoringa) {
                            Text(
                                text = "🔥 EMERGÊNCIA EXPRESS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = IndustrialOrange,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        Text(
                            text = day.diaSemana,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = IndustrialTextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = day.foco,
                            fontSize = 13.sp,
                            color = IndustrialTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Progresso do Treino",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = IndustrialTextSecondary
                            )
                            Text(
                                text = "$completedSets / $totalSets séries",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (completedSets == totalSets && totalSets > 0) IndustrialSuccess else IndustrialOrange
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (completedSets == totalSets && totalSets > 0) IndustrialSuccess else IndustrialOrange,
                            trackColor = Color(0xFF2A2A2A)
                        )
                    }
                }
            }
        }

        // Exercises
        items(day.exercicios, key = { it.id }) { exercise ->
            ExerciseCard(
                exercise = exercise,
                progress = progressMap[exercise.id],
                onWeightChanged = { setNum, weight ->
                    onWeightChanged(exercise.id, setNum, weight)
                },
                onSetCompletionToggled = { setNum, completed ->
                    onSetCompletionToggled(exercise.id, setNum, completed)
                },
                onNoteChanged = { note ->
                    onNoteChanged(exercise.id, note)
                },
                onStartRestTimer = { seconds ->
                    onStartRestTimer(seconds, exercise.nome)
                }
            )
        }

        // Bottom space so timer overlay doesn't obstruct last exercise
        item {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}
