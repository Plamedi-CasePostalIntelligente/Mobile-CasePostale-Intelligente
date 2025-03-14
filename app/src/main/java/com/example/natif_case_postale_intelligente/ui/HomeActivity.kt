package com.example.natif_case_postale_intelligente.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.mqtt.MqttManager
import com.example.natif_case_postale_intelligente.viewmodel.DeliveryViewModel
import com.example.natif_case_postale_intelligente.model.DeliveryUiState

class HomeActivity : AppCompatActivity() {

    private lateinit var deliveryRecyclerView: RecyclerView
    private lateinit var mqttStatusTextView: TextView
    private val viewModel: DeliveryViewModel by viewModels()
    private var mqttManager: MqttManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Initialisation des vues
        mqttStatusTextView = findViewById(R.id.mqttStatusTextView)
        deliveryRecyclerView = findViewById(R.id.deliveryRecyclerView)

        // Configuration initiale
        mqttStatusTextView.text = "MQTT: Initialisation..."
        deliveryRecyclerView.layoutManager = LinearLayoutManager(this)

        // Observation du ViewModel
        viewModel.deliveryState.observe(this) { state ->
            when (state) {
                is DeliveryUiState.Idle -> { /* Rien à afficher */ }
                is DeliveryUiState.Loading -> { /* Rien à afficher */ }
                is DeliveryUiState.Success -> {
                    deliveryRecyclerView.adapter = DeliveryAdapter(state.deliveries)
                    mqttStatusTextView.text = "${state.deliveries.size} livraisons chargées"
                }
                is DeliveryUiState.Error -> {
                    mqttStatusTextView.text = "Erreur API: ${state.message}" // Seulement pour les erreurs API
                }
            }
        }

        // Lancer la récupération des livraisons
        viewModel.fetchDeliveries()

        // Initialisation MQTT
        setupMqtt()
    }

    private fun setupMqtt() {
        mqttManager = MqttManager(this) { topic, message ->
            runOnUiThread {
                if (topic == "Status" || topic == "Erreur") {
                    mqttStatusTextView.text = message
                } else {
                    // On affiche juste le message brut, même s'il est invalide
                    mqttStatusTextView.text = "Message reçu sur $topic: $message"
                    viewModel.processMqttMessage(topic, message) // Appelle la méthode du ViewModel
                }
            }
        }
        mqttManager?.connect()
        mqttManager?.subscribeToTopic("test")
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttManager?.disconnect()
        mqttStatusTextView.text = "MQTT: Déconnecté"
    }
}