package com.example.natif_case_postale_intelligente.model

data class Delivery(
    val description: String,
    val expediteur: String,
    val adresse: String,
    val is_delivered: Int
)

data class DeliveryResponse(
    val success: Boolean,
    val message: String,
    val deliveries: List<Delivery>
)

sealed class DeliveryUiState {
    object Idle : DeliveryUiState()
    object Loading : DeliveryUiState()
    data class Success(val deliveries: List<Delivery>) : DeliveryUiState()
    data class Error(val message: String) : DeliveryUiState()
}