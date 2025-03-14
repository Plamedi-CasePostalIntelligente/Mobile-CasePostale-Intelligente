package com.example.natif_case_postale_intelligente.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.model.Delivery

class DeliveryAdapter(private val deliveryList: List<Delivery>) :
    RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    class DeliveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvSender: TextView = itemView.findViewById(R.id.tvSender)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvDelivered: TextView = itemView.findViewById(R.id.tvDelivered)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_delivery, parent, false)
        return DeliveryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val delivery = deliveryList[position]
        holder.tvDescription.text = "Description: ${delivery.description}"
        holder.tvSender.text = "Expéditeur: ${delivery.expediteur}"
        holder.tvAddress.text = "Adresse: ${delivery.adresse}"
        holder.tvDelivered.text = "Livré: ${if (delivery.is_delivered == 1) "Oui" else "Non"}" // Correction ici
    }

    override fun getItemCount(): Int = deliveryList.size
}