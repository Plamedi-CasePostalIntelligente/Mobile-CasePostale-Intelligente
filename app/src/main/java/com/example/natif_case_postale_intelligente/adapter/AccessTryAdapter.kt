package com.example.natif_case_postale_intelligente.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.natif_case_postale_intelligente.R
import com.example.natif_case_postale_intelligente.model.AccessTry
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AccessTryAdapter(
    private var accessTryList: List<AccessTry> // Changé en var pour permettre la mise à jour
) : RecyclerView.Adapter<AccessTryAdapter.AccessTryViewHolder>() {

    class AccessTryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUid: TextView = itemView.findViewById(R.id.tvUid)
        val tvTentativeDatetime: TextView = itemView.findViewById(R.id.tvTentativeDatetime)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessTryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_accesstry, parent, false)
        return AccessTryViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AccessTryViewHolder, position: Int) {
        val accessTry = accessTryList[position]
        holder.tvUid.text = "UID: ${accessTry.uidrfid}"

        // Formatter la date
        val dateTime = ZonedDateTime.parse(accessTry.tentativedatetime)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        holder.tvTentativeDatetime.text = "Date: ${dateTime.format(formatter)}"

        // Gérer le statut
        val statusText = when (accessTry.status) {
            1 -> "Réussi"
            0 -> "Échec"
            else -> "Inconnu"
        }
        holder.tvStatus.text = "Statut: $statusText"
        holder.tvStatus.setTextColor(
            when (accessTry.status) {
                1 -> 0xFF4CAF50.toInt() // Vert
                0 -> 0xFFF44336.toInt() // Rouge
                else -> 0xFF757575.toInt() // Gris
            }
        )
    }

    override fun getItemCount(): Int = accessTryList.size

    // Méthode pour mettre à jour la liste
    fun updateData(newList: List<AccessTry>) {
        accessTryList = newList
        notifyDataSetChanged()
    }
}