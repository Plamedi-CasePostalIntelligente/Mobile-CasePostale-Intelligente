package com.example.natif_case_postale_intelligente.model

import java.time.format.DateTimeFormatter

data class AccessTry(
    val uidrfid: String,
    val tentativedatetime: String, // Changé de DateTimeFormatter à String
    val status: Int ? = null
)

data class AccessTryResponse(
    val success: Boolean,
    val message: String,
    val accessTries: List<AccessTry>
)

sealed class AccessTryUiState {
    object Idle : AccessTryUiState()
    object Loading : AccessTryUiState()
    data class Success(val accessTries: List<AccessTry>) : AccessTryUiState()
    data class Error(val message: String) : AccessTryUiState()
}