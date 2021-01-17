package com.example.smartcane.holder

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcane.R

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @JvmField
    var namaContact: TextView

    @JvmField
    var relationContact: TextView

    @JvmField
    var numberrContact: TextView

    @JvmField
    var view: CardView

    init {
        namaContact = itemView.findViewById(R.id.name_contact)
        relationContact = itemView.findViewById(R.id.relation_contact)
        numberrContact = itemView.findViewById(R.id.number_contact)
        view = itemView.findViewById(R.id.cvMain)
    }
}