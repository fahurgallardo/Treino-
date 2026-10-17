package com.example.ui.workout

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.Exercise
import com.example.data.model.ExerciseProgressEntity
import com.example.ui.theme.IndustrialBlack
import com.example.ui.theme.IndustrialBorder
import com.example.ui.theme.IndustrialDarkCard
import com.example.ui.theme.IndustrialOrange
import com.example.ui.theme.IndustrialSuccess
import com.example.ui.theme.IndustrialSurfaceElevated
import com.example.ui.theme.IndustrialTextPrimary
import com.example.ui.theme.IndustrialTextSecondary
import com.example.ui.theme.IndustrialWarningBg
import com.example.ui.theme.IndustrialWarningBorder
import com.example.ui.theme.IndustrialWarningText

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ExerciseCard(
    exercise: Exercise,
    progress: ExerciseProgressEntity?,
    onWeightChanged: (setNum: Int, weight: String) -> Unit,
    onSetCompletionToggled: (setNum: Int, completed: Boolean) -> Unit,
    onNoteChanged: (note: String) -> Unit,
    onStartRestTimer: (seconds: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val weightsMap = remember(progress?.weightsJson) {
        progress?.getWeightsMap() ?: emptyMap()
    }
    val completedSetsMap = remember(progress?.completedSetsJson) {
        progress?.getCompletedSetsMap() ?: emptyMap()
    }
    var isVideoExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("exercise_card_${exercise.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = IndustrialDarkCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndustrialBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Title
            Text(
                text = exercise.nome,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = IndustrialOrange,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Meta badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(IndustrialSurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${exercise.series}x  ${exercise.reps}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = IndustrialTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(IndustrialSurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⏱️ Descanso: ${exercise.descansoSegundos}s",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = IndustrialTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tech note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF2A2A2A))
                    .border(
                        width = 1.dp,
                        color = Color(0xFF333333),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 12.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(28.dp)
                            .background(IndustrialOrange)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = exercise.nota,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        color = IndustrialTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            // Trainer info (if available)
            if (exercise.infoAdicional.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(IndustrialWarningBg)
                        .border(
                            width = 1.dp,
                            color = IndustrialWarningBorder,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alerta",
                            tint = IndustrialOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Obs. do Treinador: ${exercise.infoAdicional}",
                            fontSize = 12.5.sp,
                            color = IndustrialWarningText,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Video player section (if videoUrl is present)
            if (exercise.videoUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { isVideoExpanded = !isVideoExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_toggle_button_${exercise.id}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF252525),
                        contentColor = IndustrialTextPrimary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndustrialBorder))
                ) {
                    Icon(
                        imageVector = if (isVideoExpanded) Icons.Default.Videocam else Icons.Default.PlayCircleOutline,
                        contentDescription = "Vídeo do exercício",
                        tint = IndustrialOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVideoExpanded) "Ocultar Vídeo de Execução" else "🎥 Ver Execução do Movimento",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                AnimatedVisibility(
                    visible = isVideoExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .border(1.dp, IndustrialBorder, RoundedCornerShape(8.dp))
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.mediaPlaybackRequiresUserGesture = false
                                    webChromeClient = WebChromeClient()
                                    webViewClient = WebViewClient()
                                    val html = """
                                        <!DOCTYPE html>
                                        <html>
                                        <body style="margin:0;padding:0;background-color:#000;display:flex;justify-content:center;align-items:center;height:100vh;">
                                            <iframe width="100%" height="100%" src="${exercise.videoUrl}" frameborder="0" allowfullscreen></iframe>
                                        </body>
                                        </html>
                                    """.trimIndent()
                                    loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sets header & rows
            Text(
                text = "SÉRIES & CARGAS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = IndustrialTextSecondary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (s in 1..exercise.series) {
                    val weight = weightsMap[s] ?: ""
                    val isChecked = completedSetsMap[s] ?: false

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF222222))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${s}º",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) IndustrialSuccess else IndustrialTextSecondary,
                            modifier = Modifier.width(32.dp)
                        )

                        // Weight input field
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { onWeightChanged(s, it) },
                            placeholder = { Text("Carga (kg)", color = Color.Gray, fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .testTag("weight_input_${exercise.id}_set_$s"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = IndustrialTextPrimary,
                                unfocusedTextColor = IndustrialTextPrimary,
                                focusedContainerColor = IndustrialBlack,
                                unfocusedContainerColor = IndustrialBlack,
                                focusedBorderColor = IndustrialOrange,
                                unfocusedBorderColor = IndustrialBorder
                            ),
                            shape = RoundedCornerShape(6.dp)
                        )

                        // Checkbox custom touch target
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { onSetCompletionToggled(s, !isChecked) }
                                .testTag("set_checkbox_${exercise.id}_set_$s"),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isChecked) IndustrialSuccess else IndustrialBlack)
                                    .border(
                                        width = 1.5.dp,
                                        color = if (isChecked) IndustrialSuccess else IndustrialBorder,
                                        shape = RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChecked) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Série concluída",
                                        tint = IndustrialBlack,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Student note
            OutlinedTextField(
                value = progress?.customNote ?: "",
                onValueChange = onNoteChanged,
                placeholder = {
                    Text(
                        "📝 Anotação do aluno (ex: banco no 4, pegada fechada...)",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_note_input_${exercise.id}"),
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = IndustrialTextPrimary,
                    unfocusedTextColor = IndustrialTextPrimary,
                    focusedContainerColor = Color(0xFF161616),
                    unfocusedContainerColor = Color(0xFF161616),
                    focusedBorderColor = IndustrialOrange,
                    unfocusedBorderColor = IndustrialBorder
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Rest button
            OutlinedButton(
                onClick = { onStartRestTimer(exercise.descansoSegundos) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("start_rest_button_${exercise.id}"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = IndustrialOrange
                ),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndustrialOrange))
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Temporizador",
                    tint = IndustrialOrange,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INICIAR DESCANSO (${exercise.descansoSegundos}s)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
