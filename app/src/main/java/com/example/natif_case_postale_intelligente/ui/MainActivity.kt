package com.example.natif_case_postale_intelligente.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.natif_case_postale_intelligente.viewmodel.LoginViewModel
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.model.LoginUiState

class MainActivity : AppCompatActivity() {
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    private val viewModel: LoginViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtpassword)
        btnLogin = findViewById(R.id.btnlogin)
        progressBar = findViewById(R.id.progressBar)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val email = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            viewModel.login(email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginUiState.Idle -> {
                    progressBar.visibility = View.GONE
                    btnLogin.isEnabled = true
                }
                is LoginUiState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.isEnabled = false
                }
                is LoginUiState.Success -> {
                    progressBar.visibility = View.GONE
                    btnLogin.isEnabled = true
                    saveUserCredentials(state.token, state.userId)
                    showToast("Connexion réussie!")
                    println("Login State: Success - Token: ${state.token}, UserId: ${state.userId}")
                    println("Launching HomeActivity")
                    // Ajouter un délai pour laisser le Toast s'afficher
                    handler.postDelayed({
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000) // 2 secondes pour laisser le Toast se fermer
                }
                is LoginUiState.Error -> {
                    progressBar.visibility = View.GONE
                    btnLogin.isEnabled = true
                    showToast(state.message)
                }
            }
        }
    }

    private fun saveUserCredentials(token: String, userId: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("token", token)
            putString("user_id", userId)
            apply()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}