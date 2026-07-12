package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.PromptHistory
import com.example.data.model.UserProfile
import com.example.data.repository.FirebaseRepository
import com.example.data.repository.GeminiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val history: List<PromptHistory> = emptyList(),
    val currentPrompt: String? = null,
    val isGenerating: Boolean = false
)

class MainViewModel : ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val geminiRepository = GeminiRepository()

    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            if (firebaseRepository.getCurrentUserId() != null) {
                fetchUserProfile()
                fetchHistory()
            } else {
                _state.update { it.copy(userProfile = null, history = emptyList()) }
            }
        }
    }

    private suspend fun fetchUserProfile() {
        val profile = firebaseRepository.getUserProfile()
        _state.update { it.copy(userProfile = profile) }
    }

    fun fetchHistory() {
        viewModelScope.launch {
            val history = firebaseRepository.getHistory()
            _state.update { it.copy(history = history) }
        }
    }

    fun generatePrompt(type: String, projectName: String, idea: String, advanced: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val credits = _state.value.userProfile?.credits ?: 0
            if (credits < 5) {
                _state.update { it.copy(errorMessage = "Not enough credits. Please recharge.") }
                return@launch
            }

            _state.update { it.copy(isGenerating = true, errorMessage = null) }
            val result = geminiRepository.generatePrompt(type, idea, projectName, advanced)
            
            result.onSuccess { generatedText ->
                val deducted = firebaseRepository.deductCredits(5)
                if (deducted) {
                    val promptHistory = PromptHistory(
                        type = type,
                        projectName = projectName,
                        idea = idea,
                        advancedFields = advanced,
                        generatedPrompt = generatedText
                    )
                    firebaseRepository.savePrompt(promptHistory)
                    fetchUserProfile()
                    fetchHistory()
                    
                    _state.update { it.copy(currentPrompt = generatedText, isGenerating = false) }
                    onSuccess(generatedText)
                } else {
                    _state.update { it.copy(isGenerating = false, errorMessage = "Failed to deduct credits.") }
                }
            }.onFailure { err ->
                _state.update { it.copy(isGenerating = false, errorMessage = err.message ?: "Generation failed.") }
            }
        }
    }
    
    fun purchaseCredits(amount: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val success = firebaseRepository.addCredits(amount)
            if (success) {
                fetchUserProfile()
                _state.update { it.copy(isLoading = false, errorMessage = "Purchase Successful!") }
            } else {
                _state.update { it.copy(isLoading = false, errorMessage = "Purchase failed.") }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
    
    fun setCurrentPrompt(prompt: String?) {
        _state.update { it.copy(currentPrompt = prompt) }
    }
}
