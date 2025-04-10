package com.example.natif_case_postale_intelligente.repository

import com.example.natif_case_postale_intelligente.api.RetrofitClient
import com.example.natif_case_postale_intelligente.model.CityResponse
import retrofit2.Response
class CityRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getAllCity(): Response<CityResponse> {
        return apiService.getAllCities();
    }
}