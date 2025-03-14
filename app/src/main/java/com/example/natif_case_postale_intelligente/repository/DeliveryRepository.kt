package com.example.natif_case_postale_intelligente.repository

import com.example.natif_case_postale_intelligente.api.RetrofitClient
import com.example.natif_case_postale_intelligente.model.DeliveryResponse
import retrofit2.Response
class DeliveryRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getAllDelivery(): Response<DeliveryResponse> {
        return apiService.getAllDelivery();
    }
}