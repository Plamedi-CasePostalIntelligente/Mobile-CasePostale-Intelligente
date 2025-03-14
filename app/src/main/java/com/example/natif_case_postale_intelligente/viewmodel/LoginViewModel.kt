package com.example.natif_case_postale_intelligente.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.natif_case_postale_intelligente.model.LoginUiState
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import com.example.natif_case_postale_intelligente.repository.LoginRepository

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()

    private val _loginState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val loginState: LiveData<LoginUiState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginUiState.Loading

                // Validation basique des entrées
                if (email.isBlank() || password.isBlank()) {
                    _loginState.value = LoginUiState.Error("L'email et le mot de passe sont requis")
                    return@launch
                }

                val response = repository.login(email, password)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.bearerToken != null && loginResponse.userId != null) {
                        _loginState.value = LoginUiState.Success(loginResponse.bearerToken, loginResponse.userId)
                    } else {
                        _loginState.value = LoginUiState.Error(loginResponse?.message ?: "Erreur inconnue")
                    }
                } else {
                    _loginState.value = LoginUiState.Error("Échec de la connexion: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Erreur réseau: ${e.message}")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }
}