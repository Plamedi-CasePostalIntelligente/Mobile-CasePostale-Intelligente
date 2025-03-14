# casePostaleIntelligente

## Description

**casePostaleIntelligente** est une application Android innovante conçue pour gérer des livraisons via une boîte postale connectée. Elle récupère les livraisons en temps réel via une API REST et intègre des notifications MQTT pour signaler de nouvelles livraisons. Les données sont stockées dans une base de données MariaDB, avec des appels API gérés par Retrofit et une communication MQTT assurée par Fusesource MQTT.

### Fonctionnalités principales
- Récupération des livraisons via une API REST.
- Affichage des livraisons dans un `RecyclerView` avec un `DeliveryAdapter` personnalisé.
- Réception en temps réel de nouvelles livraisons via MQTT sur le topic `test` (format : `description,expediteur,adresse,is_delivered`).
- Affichage des messages MQTT dans un `TextView` (`mqttStatusTextView`).

## Prérequis

Pour exécuter ce projet, assurez-vous d’avoir les éléments suivants installés et configurés :

### Logiciels nécessaires
- **[Android Studio](https://developer.android.com/studio)** : Version récente (ex. 2023.1.1 ou supérieure).
- **[MariaDB](https://mariadb.org/download/)** : Version 10.5 ou supérieure pour la base de données.
- **[Mosquitto MQTT Broker](https://mosquitto.org/download/)** : Nécessaire pour les messages en temps réel.
- **Java 11** : Configuré dans Android Studio (spécifié dans `build.gradle`).
- **Émulateur ou appareil Android** : API 24 ou supérieure (testé sur "Medium Phone API 35").

### Dépendances du projet
Les bibliothèques suivantes sont incluses dans `build.gradle` :
- **Retrofit** : `com.squareup.retrofit2:retrofit:2.9.0` (appels API).
- **Gson Converter** : `com.squareup.retrofit2:converter-gson:2.9.0` (parsing JSON).
- **OkHttp Logging** : `com.squareup.okhttp3:logging-interceptor:4.10.0` (logs API).
- **Fusesource MQTT** : `org.fusesource.mqtt-client:mqtt-client:1.16` (communication MQTT).
- **Coroutines** : `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4` (asynchronisme).
- **AndroidX Lifecycle** : `androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1` et `androidx.lifecycle:lifecycle-livedata-ktx:2.6.1` (ViewModel et LiveData).

### Configuration réseau
- **API REST** : Une API doit être accessible (configurer l’URL dans `RetrofitClient.kt`). Elle doit renvoyer un `DeliveryResponse` au format JSON avec `success`, `message`, et `deliveries`.
- **Broker MQTT** : Configuré à `tcp://142.93.159.85:1883` avec les identifiants `cranouf`/`cranouf16@01` (modifiables dans `MqttManager.kt`).

## Installation

### 1. Cloner le projet
Clonez le repository sur votre machine :
```bash
git clone <url-du-repository>
cd casePostaleIntelligente
