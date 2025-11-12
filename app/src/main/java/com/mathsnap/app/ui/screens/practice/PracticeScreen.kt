package com.mathsnap.app.ui.screens.practice

import android.Manifest
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mathsnap.app.util.VoiceInputHelper
import com.mathsnap.app.util.rememberVoiceInputHelper

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PracticeScreen(
    operation: String,
    difficulty: String,
    navController: NavController,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val newBadges by viewModel.newBadges.collectAsState()

    // Voice input state
    var isListening by remember { mutableStateOf(false) }
    var voiceError by remember { mutableStateOf<String?>(null) }
    val voiceInputHelper = rememberVoiceInputHelper()
    val context = LocalContext.current

    // Audio permission
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    // Cleanup voice helper when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            voiceInputHelper.destroy()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Practice Mode")
                        Text(
                            text = "$operation - $difficulty",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Streak indicator
                    if (uiState.streak > 0) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔥",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${uiState.streak}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Problem Card
            uiState.currentProblem?.let { problem ->
                ProblemCard(
                    problemText = problem.problemText,
                    isCorrect = uiState.isCorrect
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Answer Input with Voice Button
            AnswerInputWithVoice(
                answer = uiState.userAnswer,
                onAnswerChange = viewModel::updateAnswer,
                enabled = !uiState.showFeedback,
                isListening = isListening,
                onVoiceClick = {
                    if (audioPermissionState.status.isGranted) {
                        if (!isListening) {
                            isListening = true
                            voiceError = null
                            voiceInputHelper.startListening(
                                onResult = { number ->
                                    viewModel.updateAnswer(number)
                                    isListening = false
                                },
                                onError = { error ->
                                    voiceError = error
                                    isListening = false
                                }
                            )
                        } else {
                            voiceInputHelper.stopListening()
                            isListening = false
                        }
                    } else {
                        audioPermissionState.launchPermissionRequest()
                    }
                }
            )

            // Voice error message
            AnimatedVisibility(
                visible = voiceError != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                voiceError?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "🎤 $error",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // Hint
            AnimatedVisibility(
                visible = uiState.hint != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                uiState.hint?.let { hint ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Text(
                            text = "💡 $hint",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = viewModel::requestHint,
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.showFeedback
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hint")
                }

                Button(
                    onClick = {
                        if (uiState.showFeedback && uiState.isCorrect == false) {
                            viewModel.generateNewProblem()
                        } else {
                            viewModel.submitAnswer()
                        }
                    },
                    modifier = Modifier.weight(2f),
                    enabled = uiState.userAnswer.isNotBlank()
                ) {
                    Text(
                        text = if (uiState.showFeedback && uiState.isCorrect == false) "Next" else "Submit",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Feedback
            AnimatedVisibility(
                visible = uiState.showFeedback,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                FeedbackCard(isCorrect = uiState.isCorrect ?: false)
            }
        }

        // Badge Notification
        if (newBadges.isNotEmpty()) {
            BadgeEarnedDialog(
                badges = newBadges,
                onDismiss = viewModel::clearBadgeNotifications
            )
        }
    }
}

@Composable
fun ProblemCard(
    problemText: String,
    isCorrect: Boolean?
) {
    val scale by animateFloatAsState(
        targetValue = if (isCorrect == true) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = when (isCorrect) {
                true -> MaterialTheme.colorScheme.primaryContainer
                false -> MaterialTheme.colorScheme.errorContainer
                null -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = problemText,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerInputWithVoice(
    answer: String,
    onAnswerChange: (String) -> Unit,
    enabled: Boolean,
    isListening: Boolean,
    onVoiceClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = answer,
            onValueChange = onAnswerChange,
            modifier = Modifier.weight(1f),
            label = { Text("Your Answer") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = enabled,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        // Voice Input Button
        FloatingActionButton(
            onClick = onVoiceClick,
            modifier = Modifier.size(64.dp),
            containerColor = if (isListening)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.secondaryContainer,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = if (isListening) 8.dp else 4.dp
            )
        ) {
            // Animated icon
            val scale by animateFloatAsState(
                targetValue = if (isListening) 1.2f else 1f,
                animationSpec = repeating(
                    animation = tween(500),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = if (isListening) "Listening..." else "Voice Input",
                modifier = Modifier.scale(scale),
                tint = if (isListening)
                    MaterialTheme.colorScheme.onError
                else
                    MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun FeedbackCard(isCorrect: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isCorrect) "🎉" else "❌",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = if (isCorrect) "Correct! Amazing!" else "Not quite, try again!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BadgeEarnedDialog(
    badges: List<com.mathsnap.app.domain.model.Badge>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Text("🏆", style = MaterialTheme.typography.displayMedium) },
        title = { Text("New Badge Earned!") },
        text = {
            Column {
                badges.forEach { badge ->
                    Text("${badge.iconEmoji} ${badge.name}: ${badge.description}")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Awesome!")
            }
        }
    )
}
