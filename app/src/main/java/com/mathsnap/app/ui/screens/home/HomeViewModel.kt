package com.mathsnap.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathsnap.app.domain.model.DailyChallenge
import com.mathsnap.app.domain.model.UserProgress
import com.mathsnap.app.domain.repository.DailyChallengeRepository
import com.mathsnap.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val userProgress: UserProgress? = null,
    val dailyChallenge: DailyChallenge? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val dailyChallengeRepository: DailyChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Load user progress
                val progress = progressRepository.getUserProgress()

                // Load today's daily challenge
                val dailyChallenge = dailyChallengeRepository.getTodayChallenge()

                _uiState.value = _uiState.value.copy(
                    userProgress = progress,
                    dailyChallenge = dailyChallenge,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun refreshData() {
        loadHomeData()
    }
}
