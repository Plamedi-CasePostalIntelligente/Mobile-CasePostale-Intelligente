package com.example.natif_case_postale_intelligente.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natif_case_postale_intelligente.model.City
import com.example.natif_case_postale_intelligente.model.CityUiState
import com.example.natif_case_postale_intelligente.repository.CityRepository
import kotlinx.coroutines.launch

class CityViewModel : ViewModel() {

    private val _cityState = MutableLiveData<CityUiState>(CityUiState.Idle)
    val cityState: LiveData<CityUiState> = _cityState

    private val cities = mutableListOf<City>()
    private val repository = CityRepository()

    fun fetchCities() {
        viewModelScope.launch {
            _cityState.value = CityUiState.Loading
            println("Début récupération des villes")
            try {
                val response = repository.getAllCity()
                println("Réponse API: ${response.isSuccessful}, Body: ${response.body()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    cities.clear()
                    response.body()?.cities?.let {
                        cities.addAll(it)
                        println("Villes récupérées: ${cities.size} - ${cities}")
                    }
                    _cityState.value = CityUiState.Success(cities)
                } else {
                    _cityState.value = CityUiState.Error("Erreur API: ${response.body()?.message ?: "Réponse invalide"}")
                }
            } catch (e: Exception) {
                _cityState.value = CityUiState.Error("Erreur chargement: ${e.message}")
                println("Exception: ${e.message}")
            }
        }
    }
}