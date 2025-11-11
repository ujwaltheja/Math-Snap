package com.mathsnap.app.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathsnap.app.domain.model.*
import com.mathsnap.app.domain.repository.BadgeRepository
import com.mathsnap.app.domain.repository.DailyChallengeRepository
import com.mathsnap.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProgressUiState(
    val userProgress: UserProgress? = null,
    val earnedBadges: List<Badge> = emptyList(),
    val completedChallenges: Int = 0,
    val operationStats: Map<MathOperation, OperationStats> = emptyMap(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository,
    private val dailyChallengeRepository: DailyChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            combine(
                progressRepository.getUserProgressFlow(),
                badgeRepository.getEarnedBadges(),
                dailyChallengeRepository.getCompletedChallengeCount()
            ) { progress, badges, challengeCount ->
                Triple(progress, badges, challengeCount)
            }.collect { (progress, badges, challengeCount) ->
                // Load operation stats
                val stats = mutableMapOf<MathOperation, OperationStats>()
                MathOperation.values().forEach { operation ->
                    progressRepository.getOperationStats(operation).first().let { stat ->
                        stats[operation] = stat
                    }
                }

                _uiState.value = ProgressUiState(
                    userProgress = progress,
                    earnedBadges = badges,
                    completedChallenges = challengeCount,
                    operationStats = stats,
                    isLoading = false
                )
            }
        }
    }
}
