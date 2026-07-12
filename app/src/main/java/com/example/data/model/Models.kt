package com.example.data.model

data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val credits: Int = 15 // Default 15 credits
)

data class PromptHistory(
    val id: String = "",
    val userId: String = "",
    val type: String = "", // "Website" or "App"
    val projectName: String = "",
    val idea: String = "",
    val advancedFields: String = "",
    val generatedPrompt: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
