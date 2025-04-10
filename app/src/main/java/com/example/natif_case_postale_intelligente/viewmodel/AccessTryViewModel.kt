package com.example.natif_case_postale_intelligente.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natif_case_postale_intelligente.model.AccessTry
import com.example.natif_case_postale_intelligente.model.AccessTryUiState
import com.example.natif_case_postale_intelligente.model.City
import com.example.natif_case_postale_intelligente.model.CityUiState
import com.example.natif_case_postale_intelligente.repository.AccessTryRepository
import com.example.natif_case_postale_intelligente.repository.CityRepository
import kotlinx.coroutines.launch

class AccessTryViewModel : ViewModel() {

    private val _accessTryState = MutableLiveData<AccessTryUiState>(AccessTryUiState.Idle)
    val accessTryState: LiveData<AccessTryUiState> = _accessTryState

    private val accessTries = mutableListOf<AccessTry>()
    private val repository = AccessTryRepository()

    fun fetchAccessTries() {
        viewModelScope.launch {
            _accessTryState.value = AccessTryUiState.Loading
            println("Début récupération des villes")
            try {
                val response = repository.getAllAccessTries()
                println("Réponse API: ${response.isSuccessful}, Body: ${response.body()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    accessTries.clear()
                    response.body()?.accessTries?.let {
                        accessTries.addAll(it)
                        println("Villes récupérées: ${accessTries.size} - ${accessTries}")
                    }
                    _accessTryState.value = AccessTryUiState.Success(accessTries)
                } else {
                    _accessTryState.value = AccessTryUiState.Error("Erreur API: ${response.body()?.message ?: "Réponse invalide"}")
                }
            } catch (e: Exception) {
                _accessTryState.value = AccessTryUiState.Error("Erreur chargement: ${e.message}")
                println("Exception: ${e.message}")
            }
        }
    }
}