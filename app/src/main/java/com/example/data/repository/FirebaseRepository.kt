package com.example.data.repository

import com.example.data.model.PromptHistory
import com.example.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth by lazy { try { FirebaseAuth.getInstance() } catch(e: Exception) { null } }
    private val firestore by lazy { try { FirebaseFirestore.getInstance() } catch(e: Exception) { null } }

    fun getCurrentUserId(): String? = auth?.currentUser?.uid ?: "mock_user_123"
    fun getCurrentUserEmail(): String? = auth?.currentUser?.email ?: "mock@example.com"
    fun getCurrentUserName(): String? = auth?.currentUser?.displayName ?: "Mock User"

    suspend fun getUserProfile(): UserProfile? {
        val uid = getCurrentUserId() ?: return null
        return try {
            val db = firestore ?: return UserProfile(uid, getCurrentUserName() ?: "", getCurrentUserEmail() ?: "", 1000)
            val doc = db.collection("users").document(uid).get().await()
            if (doc.exists()) {
                doc.toObject(UserProfile::class.java)
            } else {
                // Create default profile
                val profile = UserProfile(uid, getCurrentUserName() ?: "", getCurrentUserEmail() ?: "", 15)
                db.collection("users").document(uid).set(profile).await()
                profile
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deductCredits(amount: Int): Boolean {
        val uid = getCurrentUserId() ?: return false
        return try {
            val db = firestore ?: return true // Mock success
            val result = db.runTransaction { transaction ->
                val docRef = db.collection("users").document(uid)
                val snapshot = transaction.get(docRef)
                val currentCredits = snapshot.getLong("credits")?.toInt() ?: 0
                if (currentCredits >= amount) {
                    transaction.update(docRef, "credits", currentCredits - amount)
                    true
                } else {
                    false
                }
            }.await()
            result == true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addCredits(amount: Int): Boolean {
        val uid = getCurrentUserId() ?: return false
        return try {
            val db = firestore ?: return true // Mock success
            val docRef = db.collection("users").document(uid)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                val currentCredits = snapshot.getLong("credits")?.toInt() ?: 0
                transaction.update(docRef, "credits", currentCredits + amount)
            }.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun savePrompt(promptHistory: PromptHistory) {
        try {
            val uid = getCurrentUserId() ?: return
            val db = firestore
            if (db == null) {
                // Mock behavior: ignore save
                return
            }
            val docRef = db.collection("prompts").document()
            val historyWithId = promptHistory.copy(id = docRef.id, userId = uid)
            docRef.set(historyWithId).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getHistory(): List<PromptHistory> {
        val uid = getCurrentUserId() ?: return emptyList()
        return try {
            val db = firestore ?: return emptyList()
            val querySnapshot = db.collection("prompts")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            querySnapshot.toObjects(PromptHistory::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
