package com.mathsnap.app.ui.screens.practice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathsnap.app.domain.model.*
import com.mathsnap.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PracticeUiState(
    val currentProblem: MathProblem? = null,
    val userAnswer: String = "",
    val isCorrect: Boolean? = null,
    val showFeedback: Boolean = false,
    val hint: String? = null,
    val streak: Int = 0,
    val problemsCompleted: Int = 0,
    val startTime: Long = 0L,
    val isLoading: Boolean = false
)

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val generateProblem: GenerateProblemUseCase,
    private val checkAnswer: CheckAnswerUseCase,
    private val updateProgress: UpdateProgressUseCase,
    private val checkBadges: CheckBadgesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val operation: MathOperation = savedStateHandle.get<String>("operation")?.let {
        MathOperation.valueOf(it)
    } ?: MathOperation.ADDITION

    private val difficulty: DifficultyLevel = savedStateHandle.get<String>("difficulty")?.let {
        DifficultyLevel.valueOf(it)
    } ?: DifficultyLevel.EASY

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    private val _newBadges = MutableStateFlow<List<Badge>>(emptyList())
    val newBadges: StateFlow<List<Badge>> = _newBadges.asStateFlow()

    init {
        generateNewProblem()
    }

    fun generateNewProblem() {
        val problem = generateProblem(operation, difficulty)
        _uiState.value = PracticeUiState(
            currentProblem = problem,
            startTime = System.currentTimeMillis()
        )
    }

    fun updateAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(userAnswer = answer)
    }

    fun submitAnswer() {
        val currentState = _uiState.value
        val problem = currentState.currentProblem ?: return
        val userAnswer = currentState.userAnswer.toIntOrNull() ?: return

        viewModelScope.launch {
            val timeSpent = System.currentTimeMillis() - currentState.startTime
            val attempt = checkAnswer(problem, userAnswer, timeSpent)

            _uiState.value = currentState.copy(
                isCorrect = attempt.isCorrect,
                showFeedback = true,
                streak = if (attempt.isCorrect) currentState.streak + 1 else 0,
                problemsCompleted = currentState.problemsCompleted + 1
            )

            // Save progress
            updateProgress(attempt)

            // Check for new badges
            val earnedBadges = checkBadges()
            if (earnedBadges.isNotEmpty()) {
                _newBadges.value = earnedBadges
            }

            // Auto-advance to next problem after delay
            if (attempt.isCorrect) {
                delay(1500)
                generateNewProblem()
            }
        }
    }

    fun requestHint() {
        val problem = _uiState.value.currentProblem ?: return
        val hint = checkAnswer.generateHint(problem)
        _uiState.value = _uiState.value.copy(hint = hint)
    }

    fun clearFeedback() {
        _uiState.value = _uiState.value.copy(
            showFeedback = false,
            isCorrect = null,
            hint = null
        )
    }

    fun clearBadgeNotifications() {
        _newBadges.value = emptyList()
    }
}
