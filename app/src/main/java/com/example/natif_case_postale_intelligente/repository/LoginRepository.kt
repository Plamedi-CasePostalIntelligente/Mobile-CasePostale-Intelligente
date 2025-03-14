package com.example.natif_case_postale_intelligente.repository

import com.example.natif_case_postale_intelligente.api.RetrofitClient
import com.example.natif_case_postale_intelligente.model.LoginRequest
import com.example.natif_case_postale_intelligente.model.LoginResponse
import retrofit2.Response
class LoginRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return apiService.login(LoginRequest(email, password))
    }
}