package com.example.natif_case_postale_intelligente.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.model.City

class CityAdapter(
    private val cityList: List<City>,
    private val onCityClick: () -> Unit // Callback simplifié sans paramètre
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cityList[position]
        holder.tvCity.text = "Ville: ${city.ville}"
        // Clic sur l’item
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Ville cliquée: ${city.ville}", Toast.LENGTH_SHORT).show()
            println("Clic détecté sur la ville: ${city.ville}")
            onCityClick() // Appelle le callback sans passer la ville
        }
    }

    override fun getItemCount(): Int = cityList.size
}