package com.example.data.repository

import com.example.BuildConfig
import com.example.data.remote.Content
import com.example.data.remote.GenerateContentRequest
import com.example.data.remote.Part
import com.example.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiRepository {
    suspend fun generatePrompt(
        type: String,
        idea: String,
        projectName: String,
        advancedFields: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.failure(Exception("Gemini API Key is missing. Please configure it in AI Studio Secrets."))
            }

            val systemInstruction = "You are an expert $type prompt engineer. Generate a clean, structured, and comprehensive prompt that a developer can use to build the requested idea. Format the output professionally without markdown like ** or unwanted formatting. Just the plain structured text."
            val userPrompt = """
                Project Name: ${projectName.takeIf { it.isNotBlank() } ?: "N/A"}
                Idea: $idea
                Advanced Info (Tech Stack/Features): ${advancedFields.takeIf { it.isNotBlank() } ?: "None specified"}
                
                Please generate the complete development prompt.
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = userPrompt)))),
                systemInstruction = Content(parts = listOf(Part(text = systemInstruction)))
            )

            val response = RetrofitClient.service.generateContent(apiKey, request)
            val generatedText = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            
            if (generatedText != null) {
                Result.success(generatedText)
            } else {
                Result.failure(Exception("No valid response received from AI."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
