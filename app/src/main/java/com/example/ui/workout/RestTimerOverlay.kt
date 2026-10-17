package com.example.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndustrialBlack
import com.example.ui.theme.IndustrialBorder
import com.example.ui.theme.IndustrialOrange
import com.example.ui.theme.IndustrialOrangeDark
import com.example.ui.theme.IndustrialSuccess
import com.example.ui.timer.RestTimerState

@Composable
fun RestTimerOverlay(
    timerState: RestTimerState,
    onPauseResume: () -> Unit,
    onAddSeconds: (Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = timerState.isActive,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp))
                .border(
                    width = 2.dp,
                    color = if (timerState.isFinished) IndustrialSuccess else IndustrialOrange,
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("rest_timer_overlay"),
            shape = RoundedCornerShape(24.dp),
            color = if (timerState.isFinished) IndustrialSuccess else IndustrialOrange
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer icon",
                                tint = IndustrialBlack,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (timerState.isFinished) "DESCANSO CONCLUÍDO!" else "DESCANSO ATIVO",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = IndustrialBlack.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = if (timerState.isFinished) "GO! BORA TREINAR!" else "${timerState.remainingSeconds}s",
                                fontSize = if (timerState.isFinished) 20.sp else 28.sp,
                                fontWeight = FontWeight.Black,
                                color = IndustrialBlack,
                                modifier = Modifier.testTag("rest_timer_display")
                            )
                        }
                    }

                    // Control actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (!timerState.isFinished) {
                            // Quick +15s button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black.copy(alpha = 0.2f))
                                    .clickable { onAddSeconds(15) }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                                    .testTag("timer_add_15s_button")
                            ) {
                                Text(
                                    text = "+15s",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IndustrialBlack
                                )
                            }

                            // Pause / Play
                            IconButton(
                                onClick = onPauseResume,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.2f))
                                    .testTag("timer_pause_resume_button")
                            ) {
                                Icon(
                                    imageVector = if (timerState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                    contentDescription = if (timerState.isPaused) "Continuar" else "Pausar",
                                    tint = IndustrialBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Close button
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.25f))
                                .testTag("timer_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar timer",
                                tint = IndustrialBlack,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                if (!timerState.isFinished && timerState.totalSeconds > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    val progress = (timerState.remainingSeconds.toFloat() / timerState.totalSeconds.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = IndustrialBlack,
                        trackColor = Color.Black.copy(alpha = 0.15f)
                    )
                }
            }
        }
    }
}
