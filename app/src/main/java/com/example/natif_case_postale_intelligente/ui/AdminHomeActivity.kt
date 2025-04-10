package com.example.natif_case_postale_intelligente.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.model.CityUiState
import com.example.natif_case_postale_intelligente.viewmodel.CityViewModel

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var userIcon: ImageView
    private val viewModel: CityViewModel by viewModels()
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_admin)

        // Récupérer l'email depuis l'Intent
        userEmail = intent.getStringExtra("USER_EMAIL") ?: "Email non disponible"

        // Initialiser les vues
        cityRecyclerView = findViewById(R.id.cityRecyclerView)
        userIcon = findViewById(R.id.userIcon)

        cityRecyclerView.layoutManager = LinearLayoutManager(this)
        cityRecyclerView.adapter = CityAdapter(emptyList()) {
            startActivity(Intent(this, DashboardAdminActivity::class.java))
        }

        // Listener pour l'icône utilisateur
        userIcon.setOnClickListener {
            showUserPopupMenu()
        }

        // Observer l'état du ViewModel
        viewModel.cityState.observe(this) { state ->
            when (state) {
                is CityUiState.Idle -> { }
                is CityUiState.Loading -> { }
                is CityUiState.Success -> {
                    cityRecyclerView.adapter = CityAdapter(state.cities) {
                        startActivity(Intent(this, DashboardAdminActivity::class.java))
                    }
                }
                is CityUiState.Error -> {
                    Toast.makeText(this, "Erreur: ${state.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.fetchCities()
    }

    private fun showUserPopupMenu() {
        val popupMenu = PopupMenu(this, userIcon)
        popupMenu.menuInflater.inflate(R.menu.user_menu, popupMenu.menu)
        popupMenu.menu.findItem(R.id.user_email).title = "Utilisateur : $userEmail"

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    // Logique de déconnexion
                    val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}