package com.example.natif_case_postale_intelligente.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.mqtt.MqttManager

class DashboardAdminActivity : AppCompatActivity() {

    private lateinit var mqttTemperatureTextView: TextView
    private lateinit var mqttOledTextView: TextView
    private lateinit var mqttRfidTextView: TextView
    private lateinit var mqttUltrasonicTextView: TextView
    private lateinit var mqttManager: MqttManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashbord_admin) // Correction du nom du layout

        // Initialisation des TextViews
        mqttTemperatureTextView = findViewById(R.id.mqttTemperatureValue)
        mqttOledTextView = findViewById(R.id.mqttOledValue)
        mqttRfidTextView = findViewById(R.id.mqttRfidValue)
        mqttUltrasonicTextView = findViewById(R.id.mqttUltrasonicValue)

        // Bouton de retour
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, AdminHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Bouton pour aller vers les tentatives de connexion
        val buttonLoginAttempts = findViewById<Button>(R.id.loginAttemptsButton)
        buttonLoginAttempts.setOnClickListener{
            val intent = Intent(this, AdminAccessTryActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configuration MQTT
        setupMqtt()
    }

    private fun setupMqtt() {
        // Initialisation du MqttManager avec callback pour gérer les messages
        mqttManager = MqttManager(this) { topic, message ->
            runOnUiThread {
                // Mise à jour des TextViews en fonction du topic
                when (topic) {
                    "statutTemperature" -> mqttTemperatureTextView.text = "$message°C"
                    "statutOled" -> mqttOledTextView.text = message
                    "statutRfid" -> mqttRfidTextView.text = message
                    "statutUltrasonic" -> mqttUltrasonicTextView.text = "$message cm"
                    "Status" -> {
                        // Optionnel : afficher les messages de statut quelque part
                        mqttTemperatureTextView.text = "Statut: $message" // Par exemple
                    }
                    "Erreur" -> {
                        // Optionnel : gérer les erreurs
                        mqttTemperatureTextView.text = "Erreur: $message"
                    }
                    else -> {
                        // Topic inconnu, on peut l’afficher pour debug
                        mqttTemperatureTextView.text = "Topic inconnu: $topic - $message"
                    }
                }
            }
        }

        // Connexion au broker
        mqttManager.connect()

        // Abonnement aux 4 topics spécifiques
        mqttManager.subscribeToTopic("statutTemperature")
        mqttManager.subscribeToTopic("statutOled")
        mqttManager.subscribeToTopic("statutRfid")
        mqttManager.subscribeToTopic("statutUltrasonic")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Déconnexion propre du MQTT pour éviter les fuites
        mqttManager.disconnect()
    }
}