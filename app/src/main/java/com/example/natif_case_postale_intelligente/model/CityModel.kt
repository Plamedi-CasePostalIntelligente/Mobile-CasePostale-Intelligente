package com.example.natif_case_postale_intelligente.model

data class City(
    val ville: String,
)

data class CityResponse(
    val success: Boolean,
    val message: String,
    val cities: List<City>
)

sealed class CityUiState {
    object Idle : CityUiState()
    object Loading : CityUiState()
    data class Success(val cities: List<City>) : CityUiState()
    data class Error(val message: String) : CityUiState()
}