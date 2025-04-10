package com.example.natif_case_postale_intelligente.repository

import com.example.natif_case_postale_intelligente.api.RetrofitClient
import com.example.natif_case_postale_intelligente.model.AccessTryResponse
import retrofit2.Response
class AccessTryRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getAllAccessTries(): Response<AccessTryResponse> {
        return apiService.getAllAccessTries();
    }
}