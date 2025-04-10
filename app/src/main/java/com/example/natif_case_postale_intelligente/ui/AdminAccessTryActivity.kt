package com.example.natif_case_postale_intelligente.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.databinding.ActivityAccesstryAdminBinding
import com.example.natif_case_postale_intelligente.model.AccessTry
import com.example.natif_case_postale_intelligente.model.AccessTryUiState
import com.example.natif_case_postale_intelligente.viewmodel.AccessTryViewModel

class AdminAccessTryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccesstryAdminBinding
    private val viewModel: AccessTryViewModel by viewModels()
    private lateinit var adapter: AccessTryAdapter
    private var allAccessTries: List<AccessTry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccesstryAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser le RecyclerView
        adapter = AccessTryAdapter(emptyList())
        binding.accessTryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.accessTryRecyclerView.adapter = adapter

        // Observer l'état du ViewModel
        viewModel.accessTryState.observe(this) { state ->
            when (state) {
                is AccessTryUiState.Idle -> { }
                is AccessTryUiState.Loading -> {
                    Toast.makeText(this, "Chargement...", Toast.LENGTH_SHORT).show()
                }
                is AccessTryUiState.Success -> {
                    allAccessTries = state.accessTries
                    adapter.updateData(allAccessTries)
                }
                is AccessTryUiState.Error -> {
                    Toast.makeText(this, "Erreur: ${state.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Charger les données
        viewModel.fetchAccessTries()

        // Bouton de tri "Réussite"
        binding.sortSuccessButton.setOnClickListener {
            val filteredList = allAccessTries.filter { it.status == 1 }
            adapter.updateData(filteredList)
        }

        // Bouton de tri "Échec"
        binding.sortFailureButton.setOnClickListener {
            val filteredList = allAccessTries.filter { it.status == 0 }
            adapter.updateData(filteredList)
        }

        // Bouton de retour (menu du bas)
        binding.backToDashboardButton.setOnClickListener {
            val intent = Intent(this, DashboardAdminActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Nouveau bouton de retour (en haut)
        binding.btnBackToDashboard.setOnClickListener {
            val intent = Intent(this, DashboardAdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}