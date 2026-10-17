package com.example.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExerciseProgressEntity
import com.example.data.model.WorkoutDataSource
import com.example.data.model.WorkoutDay
import com.example.ui.theme.IndustrialBlack
import com.example.ui.theme.IndustrialBorder
import com.example.ui.theme.IndustrialBorderLight
import com.example.ui.theme.IndustrialDarkCard
import com.example.ui.theme.IndustrialOrange
import com.example.ui.theme.IndustrialSuccess
import com.example.ui.theme.IndustrialTextPrimary
import com.example.ui.theme.IndustrialTextSecondary

@Composable
fun HomeScreen(
    workoutDays: List<WorkoutDay>,
    progressMap: Map<String, ExerciseProgressEntity>,
    onDaySelected: (String) -> Unit,
    showResetDialog: Boolean,
    onRequestResetDialog: () -> Unit,
    onConfirmReset: () -> Unit,
    onDismissResetDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialBlack)
            .testTag("home_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Brand & Trainer Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                // Trainer bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(IndustrialDarkCard)
                            .border(2.dp, IndustrialOrange, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Logo Treinador",
                            tint = IndustrialOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "TREINADOR RESPONSÁVEL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndustrialTextSecondary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = WorkoutDataSource.TREINADOR,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = IndustrialTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Student badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(IndustrialDarkCard)
                        .border(1.dp, IndustrialBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = buildAnnotatedString {
                                append("Aluno(a): ")
                                withStyle(style = SpanStyle(color = IndustrialOrange, fontWeight = FontWeight.Bold)) {
                                    append(WorkoutDataSource.ALUNO)
                                }
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndustrialTextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = WorkoutDataSource.OBJETIVO,
                            fontSize = 12.5.sp,
                            color = IndustrialTextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "PROGRAMA DE TREINO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = IndustrialTextSecondary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        // Workout Days List
        items(workoutDays, key = { it.diaId }) { day ->
            val totalSets = day.exercicios.sumOf { it.series }
            val completedSets = day.exercicios.sumOf { ex ->
                progressMap[ex.id]?.getCompletedSetsMap()?.values?.count { it } ?: 0
            }
            val isAllDone = completedSets > 0 && completedSets == totalSets

            WorkoutDayCard(
                day = day,
                totalSets = totalSets,
                completedSets = completedSets,
                isAllDone = isAllDone,
                onClick = { onDaySelected(day.diaId) }
            )
        }

        // Reset Week Button
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onRequestResetDialog,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("reset_week_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = IndustrialTextSecondary
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndustrialBorder))
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reiniciar semana",
                    tint = IndustrialTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DESMARCAR CONCLUÍDOS DA SEMANA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                text = "* Suas cargas registradas e anotações serão 100% mantidas.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = onDismissResetDialog,
            containerColor = IndustrialDarkCard,
            title = {
                Text(
                    text = "Reiniciar Semana?",
                    color = IndustrialTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Deseja desmarcar todas as séries concluídas para iniciar uma nova semana? Suas cargas e anotações registradas continuarão intactas.",
                    color = IndustrialTextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirmReset,
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialOrange),
                    modifier = Modifier.testTag("confirm_reset_dialog_button")
                ) {
                    Text("Sim, Reiniciar", color = IndustrialBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissResetDialog,
                    modifier = Modifier.testTag("cancel_reset_dialog_button")
                ) {
                    Text("Cancelar", color = IndustrialTextSecondary)
                }
            }
        )
    }
}

@Composable
fun WorkoutDayCard(
    day: WorkoutDay,
    totalSets: Int,
    completedSets: Int,
    isAllDone: Boolean,
    onClick: () -> Unit
) {
    val cardBackground = if (day.isCoringa) {
        Brush.horizontalGradient(
            colors = listOf(Color(0xFF2C2C2C), Color(0xFF1E1E1E))
        )
    } else {
        Brush.linearGradient(
            colors = listOf(IndustrialDarkCard, IndustrialDarkCard)
        )
    }

    val borderLeftColor = if (day.isCoringa) IndustrialOrange else if (isAllDone) IndustrialSuccess else IndustrialBorderLight

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("workout_day_card_${day.diaId}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndustrialBorder))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackground)
        ) {
            // Left colored stripe
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(end = 1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .background(borderLeftColor)
                        .fillMaxSize()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (day.isCoringa) {
                        Text(
                            text = "🔥 EMERGÊNCIA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndustrialOrange,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }

                    Text(
                        text = day.diaSemana,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndustrialTextPrimary
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = day.foco,
                        fontSize = 12.5.sp,
                        color = IndustrialTextSecondary,
                        lineHeight = 16.sp
                    )

                    if (completedSets > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isAllDone) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Concluído",
                                    tint = IndustrialSuccess,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Treino Concluído ($completedSets/$totalSets séries)",
                                    fontSize = 11.5.sp,
                                    color = IndustrialSuccess,
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                Text(
                                    text = "$completedSets de $totalSets séries concluídas",
                                    fontSize = 11.5.sp,
                                    color = IndustrialOrange,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Abrir treino",
                    tint = IndustrialOrange,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
