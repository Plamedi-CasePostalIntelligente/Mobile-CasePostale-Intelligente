package com.example.natif_case_postale_intelligente.mqtt

import android.content.Context
import android.util.Log
import org.fusesource.mqtt.client.*

class MqttManager(context: Context, private val onMessageReceived: (String, String) -> Unit) {
    private val TAG = "MqttManager"
    private val mqtt = MQTT()
    private var connection: BlockingConnection? = null

    init {
        try {
            mqtt.setHost("tcp://142.93.159.85:1883")
            mqtt.setUserName("cranouf")
            mqtt.setPassword("cranouf16@01")
            mqtt.setClientId("AndroidClient_${System.currentTimeMillis()}") // ID unique
        } catch (e: Exception) {
            Log.e(TAG, "Erreur init MQTT: ${e.message}", e)
            onMessageReceived("Erreur", "Erreur init: ${e.message}")
        }
    }

    fun connect() {
        try {
            Log.d(TAG, "Tentative de connexion...")
            onMessageReceived("Status", "État: Connexion en cours...")
            connection = mqtt.blockingConnection()
            connection?.connect()
            Log.d(TAG, "Connecté au broker")
            onMessageReceived("Status", "État: Connecté au broker")
            startListening()
        } catch (e: Exception) {
            Log.e(TAG, "Erreur connexion: ${e.message}", e)
            onMessageReceived("Erreur", "Erreur connexion: ${e.message}")
        }
    }

    fun subscribeToTopic(topic: String) {
        try {
            connection?.subscribe(arrayOf(Topic(topic, QoS.AT_LEAST_ONCE)))
            Log.d(TAG, "Abonné au topic: $topic")
            onMessageReceived("Status", "Abonné au topic: $topic")
        } catch (e: Exception) {
            Log.e(TAG, "Erreur abonnement: ${e.message}", e)
            onMessageReceived("Erreur", "Erreur abonnement: ${e.message}")
        }
    }

    private fun startListening() {
        Thread {
            while (connection?.isConnected == true) {
                try {
                    val message = connection?.receive()
                    if (message != null) {
                        val payload = String(message.payload)
                        Log.d(TAG, "Message reçu sur ${message.topic}: $payload")
                        onMessageReceived(message.topic, payload)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur réception: ${e.message}", e)
                    onMessageReceived("Erreur", "Erreur réception: ${e.message}")
                }
            }
        }.start()
    }

    fun disconnect() {
        try {
            connection?.disconnect()
            Log.d(TAG, "Déconnecté")
            onMessageReceived("Status", "État: Déconnecté")
        } catch (e: Exception) {
            Log.e(TAG, "Erreur déconnexion: ${e.message}", e)
            onMessageReceived("Erreur", "Erreur déconnexion: ${e.message}")
        }
    }

    fun isConnected(): Boolean = connection?.isConnected ?: false
}