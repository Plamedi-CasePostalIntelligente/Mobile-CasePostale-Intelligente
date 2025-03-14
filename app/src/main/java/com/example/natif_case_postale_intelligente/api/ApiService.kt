package com.example.natif_case_postale_intelligente.api
import com.example.natif_case_postale_intelligente.model.LoginRequest
import com.example.natif_case_postale_intelligente.model.LoginResponse
import com.example.natif_case_postale_intelligente.model.DeliveryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/delivery/getAllDelivery")
    suspend fun getAllDelivery():Response<DeliveryResponse>
}

