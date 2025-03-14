package com.example.natif_case_postale_intelligente.model

data class LoginRequest (
    val email:String,
    val password:String
)

data class LoginResponse(
    val success: Boolean?,
    val message: String?,
    val bearerToken: String,
    val userId: String
)

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String, val userId: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}