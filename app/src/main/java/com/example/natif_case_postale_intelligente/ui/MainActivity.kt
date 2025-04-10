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
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.model.LoginUiState
import com.example.natif_case_postale_intelligente.viewmodel.LoginViewModel
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

class MainActivity : AppCompatActivity() {
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    private val viewModel: LoginViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private val jwtSecretKey = "speedyyyyycraaanouufffssssaaa16092104"

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
                    val email = edtUsername.text.toString().trim() // Récupérer l'email
                    saveUserCredentials(state.token, state.userId, email) // Sauvegarder l'email
                    showToast("Connexion réussie!")
                    println("Login State: Success - Token: ${state.token}, UserId: ${state.userId}, Email: $email")
                    println("Launching HomeActivity")

                    try {
                        val cleanToken = cleanToken(state.token)
                        val isAdmin = decodeIsAdminFromToken(cleanToken)
                        println("IsAdmin: $isAdmin")

                        handler.postDelayed({
                            val intent = if (isAdmin) {
                                Intent(this, AdminHomeActivity::class.java).apply {
                                    putExtra("USER_EMAIL", email) // Passer l'email
                                }
                            } else {
                                Intent(this, HomeActivity::class.java).apply {
                                    putExtra("USER_EMAIL", email) // Passer l'email aussi pour les non-admins
                                }
                            }
                            startActivity(intent)
                            finish()
                        }, 2000)
                    } catch (e: Exception) {
                        showToast("Erreur lors du décodage du token: ${e.message}")
                    }
                }
                is LoginUiState.Error -> {
                    progressBar.visibility = View.GONE
                    btnLogin.isEnabled = true
                    showToast(state.message)
                }
            }
        }
    }

    private fun cleanToken(token: String): String {
        return if (token.startsWith("Bearer ")) {
            token.substring(7)
        } else {
            token
        }
    }

    private fun decodeIsAdminFromToken(token: String): Boolean {
        try {
            val key = Keys.hmacShaKeyFor(jwtSecretKey.toByteArray())
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .body

            println("Claims: ${claims.entries}")
            val isAdminRaw = claims.get("isAdmin")
            println("isAdmin brut: $isAdminRaw, type: ${isAdminRaw?.javaClass}")

            return when (isAdminRaw) {
                is Boolean -> isAdminRaw
                is Int -> isAdminRaw == 1
                is String -> isAdminRaw == "1"
                else -> false
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Erreur lors du décodage du token : ${e.message}", e)
        }
    }

    private fun saveUserCredentials(token: String, userId: String, email: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("token", token)
            putString("user_id", userId)
            putString("email", email) // Sauvegarder l'email
            apply()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}