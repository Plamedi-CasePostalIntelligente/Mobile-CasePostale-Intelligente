package com.example.natif_case_postale_intelligente.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natif_case_postale_intelligente.model.Delivery
import com.example.natif_case_postale_intelligente.model.DeliveryUiState
import com.example.natif_case_postale_intelligente.repository.DeliveryRepository
import kotlinx.coroutines.launch

class DeliveryViewModel : ViewModel() {

    private val _deliveryState = MutableLiveData<DeliveryUiState>(DeliveryUiState.Idle)
    val deliveryState: LiveData<DeliveryUiState> = _deliveryState

    private val deliveries = mutableListOf<Delivery>()
    private val repository = DeliveryRepository()

    fun fetchDeliveries() {
        viewModelScope.launch {
            _deliveryState.value = DeliveryUiState.Loading
            try {
                val response = repository.getAllDelivery()
                if (response.isSuccessful && response.body()?.success == true) {
                    deliveries.clear()
                    response.body()?.deliveries?.let { deliveries.addAll(it) }
                    _deliveryState.value = DeliveryUiState.Success(deliveries)
                } else {
                    _deliveryState.value = DeliveryUiState.Error("Erreur API: ${response.body()?.message ?: "Réponse invalide"}")
                }
            } catch (e: Exception) {
                _deliveryState.value = DeliveryUiState.Error("Erreur chargement: ${e.message}")
            }
        }
    }

    fun processMqttMessage(topic: String, message: String) {
        viewModelScope.launch {
            // On essaie de parser le message MQTT
            val parts = message.split(",")
            if (parts.size == 4) {
                val newDelivery = Delivery(
                    description = parts[0],
                    expediteur = parts[1],
                    adresse = parts[2],
                    is_delivered = parts[3].toIntOrNull() ?: 0
                )
                deliveries.add(newDelivery)
                _deliveryState.value = DeliveryUiState.Success(deliveries)
            } else {
                // Si le format est invalide, on ne met plus d'erreur dans l'UI
                // On laisse HomeActivity gérer l'affichage du message brut
            }
        }
    }
}